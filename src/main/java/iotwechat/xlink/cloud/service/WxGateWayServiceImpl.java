package iotwechat.xlink.cloud.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import iotwechat.xlink.cloud.api.DeviceApi;
import iotwechat.xlink.cloud.consts.WxConfig;
import iotwechat.xlink.cloud.domain.AccessToken;
import iotwechat.xlink.cloud.domain.Device;
import iotwechat.xlink.cloud.domain.DeviceAuth;
import iotwechat.xlink.cloud.domain.GateWayWx;
import iotwechat.xlink.cloud.domain.Product;
import iotwechat.xlink.cloud.domain.ProductGateWayWx;
import iotwechat.xlink.cloud.repository.DeviceRepository;
import iotwechat.xlink.cloud.repository.ProductGateWayWxRepository;
import iotwechat.xlink.cloud.repository.ProductRepository;
import iotwechat.xlink.cloud.repository.ProductWxGwRepository;
import iotwechat.xlink.cloud.repository.WxGateWayRepository;
import iotwechat.xlink.cloud.util.AccessTokenHelper;
import iotwechat.xlink.cloud.util.DeviceAuthThread;
import iotwechat.xlink.cloud.util.ParseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
@Component
public class WxGateWayServiceImpl {
	@Value("${linux.config.path}")
	private String imagePath_linux;
	
	@Value("${window.config.path}")
	private String imagePath_window;
	
	@Value("${osx.config.path}")
	private String imagePath_OSX;
	
	@Inject
	private ProductWxGwRepository productWxGwRepository;
	@Inject
	private DeviceRepository deviceRepository;
	@Inject
	private WxGateWayRepository wxGateWayRepository;
	@Inject
	private ProductRepository prodcutRepository;
	@Inject
	private ProductGateWayWxRepository productGateWayWxRepository;
	@Inject
	private AccessTokenHelper accessTokenHelper;
	
	private Logger logger = Logger.getLogger(WxGateWayServiceImpl.class);
	
	/**
	 * 设备授权
	 * @param node
	 * @return
	 */
	public HashMap<Object, Object> devicesAuth(JsonNode node) {
		HashMap<Object, Object> returnMap = new HashMap<Object, Object>();
		String  tokenId = node.path("tokenId").asText();
		String appid=node.path("appid").asText();
	
		AccessToken accessToken = accessTokenHelper.getAccessTokenWithTokenId(tokenId);
		if( accessToken != null ) {
			logger.info("tokenId:"+tokenId+"，appId:" + accessToken.getAppid()+",Appsecret:"+accessToken.getAppsecret());
			JsonNode pids = node.path("pids");
			JSONArray pidArr = JSONArray.fromObject(pids.toString());
			try {
				for(Object prodcut:pidArr){
					JSONObject jso = JSONObject.fromObject(prodcut);
					String pid=jso.getString("pid");
					
					Product product = prodcutRepository.findById(pid);
					if( product == null ) {
						returnMap.put("stauts", "403");
						returnMap.put("msg", "prodcut no found!");
						return returnMap;
					}
					ProductGateWayWx productGateWayWx = productGateWayWxRepository.findByProductId(pid);
					if( productGateWayWx == null ) {
						returnMap.put("stauts", "404");
						returnMap.put("msg", "prodcut detail no found!");
						return returnMap;
					}
					
					boolean newDevice = true;
					for( int i = 0; i < 2; i ++ ) {
						List<Device> devices = null;
						if( newDevice ) {	
							// 查找数据库中没有tokenId的条目，表示是新设备
							devices = deviceRepository.findByPidAndWxTokenIdsNotLike(pid,"%"+tokenId+"%");
						} else {
							// 查找数据库中已经有tokenId的条目，表示是久设备
							devices = deviceRepository.findByPidAndWxTokenIdsLike(pid,"%"+tokenId+"%");
						}
						// 有效的devices才处理
						if( devices != null && devices.size() > 0 ) {
							GateWayWx wxGateWay=wxGateWayRepository.findByTokenId(tokenId);
							ProductGateWayWx pwg = productWxGwRepository.findByProductIdAndGateWayWxId(pid,wxGateWay.getId());
							String imagePath="";
							String loadPath="";
							if (new File(imagePath_linux).exists()) {
								loadPath=imagePath_linux.substring(0, imagePath_linux.lastIndexOf("/"))
										+"/qrImage/"+pwg.getGateWayWxId()+"/";
								imagePath=loadPath+pid+"/";
								wxGateWay.setQrImgDir(loadPath);
							} else if( new File(imagePath_OSX).exists()) {
								loadPath=imagePath_OSX.substring(0, imagePath_OSX.lastIndexOf("/"))
										+"/qrImage/"+pwg.getGateWayWxId()+"/";
								imagePath=loadPath+pid+"/";
								wxGateWay.setQrImgDir(loadPath);
							} else {
								loadPath="E:/work/image/"+pwg.getGateWayWxId()+"/";
								imagePath=loadPath+pid+"/";
								wxGateWay.setQrImgDir(loadPath);
							}
							File dir = new File(imagePath);
							if (!dir.exists()) {
								dir.mkdirs();
							}
							
							wxGateWayRepository.save(wxGateWay);
							
							//每个产品开启一个线程，执行设备授权请求和生成二维码
							DeviceAuthThread deviceAuthThread = new DeviceAuthThread(devices, tokenId, imagePath, deviceRepository, accessTokenHelper, newDevice, product, productGateWayWx);
							deviceAuthThread.start();
						}
						// 切换查找条件的一定得在
						newDevice = false;
					}
				}
				returnMap.put("stauts", "200");
				returnMap.put("msg", "ok!");
			} catch (Exception e) {
				returnMap.put("stauts", "500");
				returnMap.put("msg", "fail!");
				e.printStackTrace();
			}
		} else {
			logger.warn("No token found when devicesAuth!");
		}
		return returnMap;
	}
}
