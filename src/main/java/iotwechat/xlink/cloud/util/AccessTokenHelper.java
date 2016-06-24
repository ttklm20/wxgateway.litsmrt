package iotwechat.xlink.cloud.util;

import java.util.HashMap;
import java.util.Map;

import iotwechat.xlink.cloud.api.MpApi;
import iotwechat.xlink.cloud.domain.AccessToken;
import iotwechat.xlink.cloud.domain.GateWayWx;
import iotwechat.xlink.cloud.redis.RedisBase;
import iotwechat.xlink.cloud.repository.WxGateWayRepository;

import javax.inject.Inject;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import common.Logger;

@Component
public class AccessTokenHelper {
	@Inject
	private RedisBase redisBase;
	@Inject
	private WxGateWayRepository wxGateWayRepository;

	private Map<String, GateWayWx> tokenIdMap = new HashMap<String, GateWayWx>();

	private Logger logger = Logger.getLogger(AccessTokenHelper.class);

	/**
	 * 通过tokenid拿取jsapi_ticket
	 * 
	 * @param tokenId
	 * @return
	 */
	public String getJSAPITicketWithTokenId(String tokenId) {
		// 先从tokenId拿取access_code
		AccessToken accessToken = getAccessTokenWithTokenId(tokenId);
		if (accessToken == null) {
			logger.error("null access_token when get jsapi ticket");
			return null;
		} else {
			String accessTokenString = accessToken.getAccess_token();
			String ticket = getJSAPITicketWithAccessToken(accessTokenString);
			// hash signature
			return ticket;
		}
	}
	
	/**
	 * 通过appId拿取jsapi_ticket
	 * @param appId
	 * @return
	 */
	public String getJSAPITicketWithAppId(String appId, String secretKey) {
		// 先从tokenId拿取access_code
		AccessToken accessToken = getAccessTokenWithAppId(appId, secretKey);
		if (accessToken == null) {
			logger.error("null access_token when get jsapi ticket by appId"
					+ appId);
			return null;
		} else {
			String accessTokenString = accessToken.getAccess_token();
			String ticket = getJSAPITicketWithAccessToken(accessTokenString);
			// hash signature
			return ticket;
		}
	}

	/**
	 * 通过TokenId拿取AccessToken
	 * 
	 * @param tokenId
	 * @return
	 */
	public AccessToken getAccessTokenWithTokenId(String tokenId) {
		// 先从tokenId拿取appid
		GateWayWx wxGW = tokenIdMap.get(tokenId);
		if (wxGW == null) {
			// 从数据库拿取tokenid对应的appid
			wxGW = wxGateWayRepository.findByTokenId(tokenId);
			if (wxGW == null) {
				logger.error("Can't find wxgateway with tokenId: " + tokenId);
				return null;
			}
			tokenIdMap.put(tokenId, wxGW);
		}
		String appid = wxGW.getAppID();
		// 不管怎样都从redis里面拿取
		AccessToken retToken = getAccessTokenFromRedis(appid);
		// 如果token是空的，就要刷新了
		if (retToken == null) {
			String secretKey = wxGW.getAppsecret();
			retToken = getAccessTokenFromWX(appid, secretKey);
		}

		return retToken;
	}

	/**
	 * 直接从AppId拿取AccessToken
	 * @param appId
	 * @param secretKey
	 * @return
	 */
	public AccessToken getAccessTokenWithAppId(String appId, String secretKey) {
		// 不管怎样都从redis里面拿取
		AccessToken retToken = getAccessTokenFromRedis(appId);
		// 如果token是空的，就要刷新了
		if (retToken == null) {
			retToken = getAccessTokenFromWX(appId, secretKey);
		}

		return retToken;
	}

	/**
	 * 通过清理这个tokenId下的access_token
	 * 
	 * @param tokenId
	 */
	void clearAccessTokenWithTokenId(String tokenId) {
		GateWayWx wxGW = getAppIdWithTokenId(tokenId);
		if( wxGW != null ) {
			String appid = wxGW.getAppID();
			AccessToken aToken = getAccessTokenFromRedis(appid);
			String key = "access-token-" + appid;
			// 删掉这个key
			redisBase.delKeyFromDB(1, key);
			// 再删掉accesstoken对应的ticket
			if( aToken != null ) {
				String accessTokenString = aToken.getAccess_token();
				key = "jsapi-ticket-" + accessTokenString;
				redisBase.delKeyFromDB(1, key);
			}
		}
	}

	/**
	 * 
	 * @param tokenId
	 * @return
	 */
	private GateWayWx getAppIdWithTokenId(String tokenId) {
		// 先从tokenId拿取appid
		GateWayWx wxGW = tokenIdMap.get(tokenId);
		if (wxGW == null) {
			// 从数据库拿取tokenid对应的appid
			wxGW = wxGateWayRepository.findByTokenId(tokenId);
			if (wxGW == null) {
				logger.error("Can't find wxgateway with tokenId: " + tokenId);
				return null;
			}
			tokenIdMap.put(tokenId, wxGW);
		}
		return wxGW;
	}

	/**
	 * 从微信服务器拿取新的access_token
	 * 
	 * @param appid
	 * @param secretKey
	 * @return
	 */
	private AccessToken getAccessTokenFromWX(String appid, String secretKey) {
		String url = MpApi.GetAccessTokenUrl.replace("APPID", appid).replace(
				"APPSECRET", secretKey);
		logger.debug("request wx access_token: " + url);
		String resultContent = HttpUtil.executeGetToke(url);
		logger.debug("response wx access_token: " + resultContent);
		JSONObject result = JSONObject.fromObject(resultContent);
		if (result.containsKey("access_token")) {
			// 成功获取信息，纪录下来
			int expire = result.getInt("expires_in");
			this.saveAccessTokenToRedis(appid, resultContent, expire);
			return AccessToken.fromJson(resultContent);
		} else {
			return null;
		}
	}

	private String getJSAPITicketWithAccessToken(String accessTokenString) {
		// 先从redis里面拿取
		String ticket = getJSAPITicketFromRedis(accessTokenString);
		// 没有ticket，再去api里面刷
		if (ticket == null || ticket.isEmpty()) {
			String url = MpApi.GetJs_ticket;
			String realUrl = url.replace("ACCESS_TOKEN", accessTokenString);
			logger.debug("request wx jsapi_ticket: " + realUrl);
			String resultContent = HttpUtil.executeGetToke(realUrl);
			logger.debug("response wx jsapi_ticket: " + resultContent);
			JSONObject result = JSONObject.fromObject(resultContent);
			if (result.containsKey("errcode") && result.getInt("errcode") == 0
					&& result.containsKey("ticket")) {
				int expire = result.getInt("expires_in");
				ticket = result.getString("ticket");
				saveJSAPITicketToRedis(accessTokenString, ticket, expire);
			}
		}

		return ticket;
	}

	/**
	 * 从Redis里面拿取AccessToken
	 * 
	 * @param appid
	 *            微信的appid
	 * @return
	 */
	private AccessToken getAccessTokenFromRedis(String appid) {
		String key = "access-token-" + appid;
		String value = redisBase.getValueFromDB(1, key);
		if (value != null) {
			logger.info("got accesstoken in redis " + value);
			AccessToken newAccessToken = AccessToken.fromJson(value);
			return newAccessToken;
		} else {
			logger.info("no accesstoken in redis " + appid);
			return null;
		}

	}

	/**
	 * 将accesstoken存储到redis
	 * 
	 * @param tokenId
	 */
	private void saveAccessTokenToRedis(String appid, String tokenData,
			int expire) {
		String key = "access-token-" + appid;
		redisBase.setValueToDB(1, key, tokenData);
		redisBase.setKeyExpireInDB(1, key, expire * 2 / 3);
	}

	/**
	 * 从Redis里面拿取jsapi_ticket
	 * 
	 * @param appid
	 *            微信的appid
	 * @return
	 */
	private String getJSAPITicketFromRedis(String accessTokenString) {
		String key = "jsapi-ticket-" + accessTokenString;
		String ticket = redisBase.getValueFromDB(1, key);
		if (ticket == null) {
			logger.info("no jsapi_ticket in redis " + accessTokenString);
		} else {
			logger.info("got jsapi_ticket in redis " + ticket);
		}
		return ticket;
	}

	/**
	 * 将jsap_ticket保存到redis里面
	 * 
	 * @param accessTokenString
	 * @param ticket
	 * @param expire
	 */
	private void saveJSAPITicketToRedis(String accessTokenString,
			String ticket, int expire) {
		String key = "jsapi-ticket-" + accessTokenString;
		redisBase.setValueToDB(1, key, ticket);
		redisBase.setKeyExpireInDB(1, key, expire * 2 / 3);
	}
}
