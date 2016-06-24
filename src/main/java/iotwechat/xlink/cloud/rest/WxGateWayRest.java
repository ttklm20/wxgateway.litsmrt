package iotwechat.xlink.cloud.rest;

import iotwechat.xlink.cloud.api.DeviceApi;
import iotwechat.xlink.cloud.consts.WxConfig;
import iotwechat.xlink.cloud.domain.AccessToken;
import iotwechat.xlink.cloud.domain.GateWayWx;
import iotwechat.xlink.cloud.domain.Product;
import iotwechat.xlink.cloud.domain.ProductGateWayWx;
import iotwechat.xlink.cloud.repository.ProductRepository;
import iotwechat.xlink.cloud.repository.ProductWxGwRepository;
import iotwechat.xlink.cloud.repository.WxGateWayRepository;
import iotwechat.xlink.cloud.service.WxGateWayService;
import iotwechat.xlink.cloud.service.WxGateWayServiceImpl;
import iotwechat.xlink.cloud.util.AccessTokenHelper;
import iotwechat.xlink.cloud.util.Base64;
import iotwechat.xlink.cloud.util.HttpAccessTokenException;
import iotwechat.xlink.cloud.util.HttpUtil;
import iotwechat.xlink.cloud.util.ParseUtil;
import iotwechat.xlink.cloud.util.ResponseCommonCode;
import iotwechat.xlink.cloud.util.SHA1;
import iotwechat.xlink.cloud.util.WeChatV1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//import sun.misc.BASE64Encoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
public class WxGateWayRest extends BaseRestController {
	@Inject
	private AccessTokenHelper accessTokenHelper;
	@Inject
	private WxGateWayServiceImpl wxGateWayServiceImpl;
	@Inject
	private WxGateWayRepository wxGateWayRepository;
	@Inject
	private ProductWxGwRepository productWxGwRepository;
	@Inject
	private ProductRepository prodcutRepository;
	
	@Value("${linux.config.path}")
	private String imagePath_linux;
	
	@Value("${window.config.path}")
	private String imagePath_window;
	
	@Value("${osx.config.path}")
	private String imagePath_OSX;
	
	private Logger logger = Logger.getLogger(WxGateWayRest.class);

	/**
	 * 将设备在微信平台中批量授权
	 * 
	 * @param body
	 * @param req
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/wxgateway/deviceAuth", method = RequestMethod.POST)
	public HashMap<Object, Object> deviceAuth(@RequestBody String body,
			HttpServletRequest req) throws JsonProcessingException, IOException {
		HashMap<Object, Object> returnMap = new HashMap<Object, Object>();
		Object obj = parseBody(body);
		if (obj instanceof HashMap) {
			return (HashMap) obj;
		}
		JsonNode devicesNode = (JsonNode) obj;
		if (devicesNode.path("appid").asText() == null
				|| devicesNode.path("appsecret").asText() == null) {
			returnMap.put("status", ResponseCommonCode._400[1]);
			returnMap.put("msg", ResponseCommonCode._400[2]);
			return returnMap;
		}
		// 批量授权
		HashMap<Object, Object> rs = wxGateWayServiceImpl
				.devicesAuth(devicesNode);
		// 返回信息
		if (rs.get("status") == "200") {
			return null;
		} else {
			return rs;
		}
	}

	/**
	 * h5页面，通过ajax跨域，获取当前页面signature
	 * @param tokenId
	 * @param nonceStr
	 * @param timestamp
	 * @param url
	 * @param signatureCallback
	 * @param req
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	@RequestMapping(value = "/wxgateway/urlSignature", method = RequestMethod.GET)
	public String urlSignature(String tokenId, String appId, String secretKey, String nonceStr,
			String timestamp, String url, String signatureCallback,
			HttpServletRequest req) throws JsonProcessingException, IOException {

		JSONObject ret = new JSONObject();

		if (tokenId.isEmpty() ||
			nonceStr.isEmpty() ||
			timestamp.isEmpty() ||
			url.isEmpty()) {
			ret.put("status", 405);
			ret.put("msg", "param error");
		} else {
			if (tokenId != null && !tokenId.isEmpty()) {
				// 先拿取ticket
				String jsapi_ticket = accessTokenHelper
						.getJSAPITicketWithTokenId(tokenId);
				if (jsapi_ticket == null) {
					ret.put("status", 406);
					ret.put("msg", "jsapi_ticket error");
				} else {
					// 计算signature
					String abcString = "jsapi_ticket=" + jsapi_ticket + "&"
							+ "noncestr=" + nonceStr + "&" + "timestamp="
							+ timestamp + "&" + "url=" + url;

					logger.info("try signagure: " + abcString);

					String qm = "";
					try {
						qm = SHA1.gen(abcString);
						ret.put("status", 200);
						ret.put("msg", "ok");
						ret.put("signature", qm);
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
						ret.put("status", 405);
						ret.put("msg", "internal error!");
					}
				}
			} else if( appId != null && !appId.isEmpty() && secretKey != null && !secretKey.isEmpty() ) {
				// 先拿取ticket
				String jsapi_ticket = accessTokenHelper
						.getJSAPITicketWithAppId(appId, secretKey);
				if (jsapi_ticket == null) {
					ret.put("status", 406);
					ret.put("msg", "jsapi_ticket error");
				} else {
					// 计算signature
					String abcString = "jsapi_ticket=" + jsapi_ticket + "&"
							+ "noncestr=" + nonceStr + "&" + "timestamp="
							+ timestamp + "&" + "url=" + url;

					logger.info("try signagure: " + abcString);

					String qm = "";
					try {
						qm = SHA1.gen(abcString);
						ret.put("status", 200);
						ret.put("msg", "ok");
						ret.put("signature", qm);
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
						ret.put("status", 405);
						ret.put("msg", "internal error!");
					}
				}
			}
		}

		// 拿到了数据，返回
		if (!signatureCallback.isEmpty()) {
			String r = String.format("%s(%s)", signatureCallback, ret.toString());
			return r;
		} else {
			return ret.toString();
		}
	}
	
	@RequestMapping(value = "/wxgateway/authDevice", method = RequestMethod.GET)
	public String authDevice(String tokenId, String pid, String deviceMac, String deviceId, String updateDevice, String signatureCallback,
			HttpServletRequest req) throws JsonProcessingException, IOException {
	
		JSONObject ret = new JSONObject();

		// 判断参数是否正确
		if (tokenId.isEmpty() || deviceMac.isEmpty() || deviceId.isEmpty()
			 || pid.isEmpty()  ) {
			ret.put("status", 405);
			ret.put("msg", "param error");
		} else {
			// 通过tokenId获取accessToken
			AccessToken accessToken = accessTokenHelper.getAccessTokenWithTokenId(tokenId);
			if (accessToken == null) {
				ret.put("status", 406);
				ret.put("msg", "access token not found");
			} else {
				GateWayWx gateWayWx = wxGateWayRepository.findByTokenId(tokenId);
				if( gateWayWx == null ) {
					ret.put("status", 407);
					ret.put("msg", "weixin gate way not found");
				} else {
					Product product = prodcutRepository.findById(pid);
					ProductGateWayWx pwg = productWxGwRepository.findByProductIdAndGateWayWxId(pid,gateWayWx.getId());
					if( product == null || pwg == null ) {
						ret.put("status", 407);
						ret.put("msg", "weixin product gate way not found");
					} else {
						
						// 如果不是更新设备数据，就先尝试拿取本地文件
						String base64Data = null;
						if( updateDevice.equals("0") ) {
							base64Data = getExistsQrImage(pid, deviceMac, deviceId, pwg);
						}
						// 直接返回已有数据
						if( base64Data != null && !base64Data.isEmpty() ) {
							logger.info("Return exists qrImage data.");
							ret.put("status", 200);
							ret.put("msg", "ok");
							ret.put("qrBase64", base64Data);
						}else {
							logger.info("Gen new qrImage data tokenId : " + tokenId + "; pid : " + pid);
							/*
							 * POST数据示例如下：
								{
								    "device_num":"1",
								    "device_list":[
								        {
								            "id":"dev1",
								            "mac":"123456789ABC",
								            "connect_protocol":"1|2",
								            "auth_key":"",
								            "close_strategy":"1",
								            "conn_strategy":"1",
								            "crypt_method":"0",
								            "auth_ver":"1",
								            "manu_mac_pos":"-1",
								            "ser_mac_pos":"-2"
								        }
								    ],
								    "op_type":"0"
								}
							 */
							JSONObject device = new JSONObject();
							device.put("id", deviceId);
							device.put("mac", deviceMac);
							if( product.getType() == 3 ) {
								// 蓝牙设备
								device.put("connect_protocol", "3");
							} else {
								// wifi设备
								device.put("connect_protocol", "4");
							}
							device.put("auth_key", pwg.getAuthKey());
							device.put("close_strategy", pwg.getCloseStrategy());
							device.put("conn_strategy", pwg.getConnStrategy());
							device.put("crypt_method", pwg.getCryptMethod());
							device.put("auth_ver", pwg.getAuthVer());
							device.put("manu_mac_pos", pwg.getManuMacPos());
							device.put("ser_mac_pos", pwg.getSerMacPos());
							JSONObject request = new JSONObject();
							request.put("device_num", "1");
							JSONArray devices = new JSONArray();
							devices.add(device);
							request.put("device_list", devices);
							request.put("op_type", updateDevice);
							
							// 提交
							try {
								ret.put("status", 501);
								ret.put("msg", "Weixin error!");
								
								JSONObject authRet = JSONObject.fromObject(HttpUtil.doPost(DeviceApi.AuthorizeUrl, request.toString(), tokenId, accessToken.getAccess_token()));
								JSONArray resp = (JSONArray) authRet.get("resp");
								if( resp.size() > 0 ) {
									JSONObject deviceResp = (JSONObject)resp.get(0);
									if( deviceResp.getInt("errcode") == 0 ) {
										
										// 再获取QRTicket
										JSONObject json = new JSONObject();
										json.put("device_num", "1");
										JSONArray device_id_list = new JSONArray();
										device_id_list.add(deviceId);
										json.put("device_id_list", device_id_list);
										JSONObject qrResp = JSONObject.fromObject(HttpUtil.doPost(DeviceApi.CreateQrcode, json.toString(),tokenId, accessToken.getAccess_token()));
										if(qrResp.getInt("errcode") != 0 ) {
											ret.put("status", 502);
											ret.put("msg", "Get device qr image error!");
											ret.put("errcode", deviceResp.get("errcode"));
											ret.put("errmsg", deviceResp.get("errmsg"));
										} else {
											JSONArray code_list = (JSONArray)qrResp.get("code_list");
											if( code_list == null || code_list.size() == 0 ) {
												ret.put("status", 503);
												ret.put("msg", "Get device qr image ticket error!");
											} else {
												JSONObject qrTicket = code_list.getJSONObject(0);
												String ticket = qrTicket.getString("ticket");
												
												// 下载二维码
												try {
													String qrImageBase64 = downloadQrImage(ticket, pid, deviceMac, deviceId, pwg);
													ret.put("status", 200);
													ret.put("msg", "ok");
													ret.put("qrBase64", qrImageBase64);
												} catch (Exception e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
													ret.put("status", 504);
													ret.put("msg", "Get device qr image base64 error!");
												}
											}
										}
									} else {
										ret.put("status", 502);
										ret.put("msg", "Auth device error!");
										ret.put("errcode", deviceResp.get("errcode"));
										ret.put("errmsg", deviceResp.get("errmsg"));
									}
								}
								
							} catch (HttpAccessTokenException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								
								ret.put("status", 500);
								ret.put("msg", "Internal error!");
							}
						}
					}
				}
			}
		}

		// 拿到了数据，返回
		if (!signatureCallback.isEmpty()) {
			String r = String.format("%s(%s)", signatureCallback,
					ret.toString());
			return r;
		} else {
			return ret.toString();
		}
	}
	
	private String getImagePath(String pid, ProductGateWayWx pwg) {
		String imagePath="";
		String loadPath="";
		if (new File(imagePath_linux).exists()) {
			loadPath=imagePath_linux.substring(0, imagePath_linux.lastIndexOf("/"))
					+"/qrImage/"+pwg.getGateWayWxId()+"/";
			imagePath=loadPath+pid+"/";
			
		} else if( new File(imagePath_OSX).exists()) {
			loadPath=imagePath_OSX.substring(0, imagePath_OSX.lastIndexOf("/"))
					+"/qrImage/"+pwg.getGateWayWxId()+"/";
			imagePath=loadPath+pid+"/";
			
		} else {
			loadPath="E:/work/image/"+pwg.getGateWayWxId()+"/";
			imagePath=loadPath+pid+"/";
		}
		File dir = new File(imagePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		return imagePath;
	}
	
	private String getExistsQrImage(String pid, String deviceMac, String deviceId, ProductGateWayWx pwg) {
		
		String path = getImagePath(pid, pwg);
		
		String qrImageFilePath = ParseUtil.getQrImageFilePath(path, deviceMac, deviceId, "jpg");
		
		File imageFile = new File(qrImageFilePath);
		if( imageFile.exists() ) {
			try {
				return encodeBase64File(qrImageFilePath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return "";
	}

	private String downloadQrImage(String ticket, String pid, String deviceMac,
			String deviceId, ProductGateWayWx pwg) throws Exception {
		
		String imagePath = getImagePath(pid, pwg);
		
		String qrImageFilePath = ParseUtil.createQrImage(imagePath, deviceMac, deviceId, ticket);
		
		return encodeBase64File(qrImageFilePath);
	}
	
	public String encodeBase64File(String path) throws Exception {
        File  file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
//        return new BASE64Encoder().encode(buffer);
        return Base64.encode(buffer);
    }
}
