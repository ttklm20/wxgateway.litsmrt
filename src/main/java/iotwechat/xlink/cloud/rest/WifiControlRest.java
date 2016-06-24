package iotwechat.xlink.cloud.rest;

import java.util.Map;

import iotwechat.xlink.cloud.domain.BoundInfo;
import iotwechat.xlink.cloud.domain.Device;
import iotwechat.xlink.cloud.domain.GateWayWx;
import iotwechat.xlink.cloud.domain.ProductGateWayWx;
import iotwechat.xlink.cloud.repository.DeviceRepository;
import iotwechat.xlink.cloud.repository.ProductWxGwRepository;
import iotwechat.xlink.cloud.repository.WxGateWayRepository;
import iotwechat.xlink.cloud.service.BoundInfoService;
import iotwechat.xlink.cloud.service.WxGateWayService;
import iotwechat.xlink.cloud.util.HttpUtil;
import iotwechat.xlink.cloud.util.MD5Util;
import iotwechat.xlink.cloud.util.WeChatV1;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**
 * Html页面请求控制设备
 * @author william
 *
 */
@RestController
public class WifiControlRest {
	@Inject
	private BoundInfoService boundInfoService;
	@Inject
	private DeviceRepository deviceRepository;
	@Inject
	private WxGateWayRepository wxGateWayRepository;
	@Inject
	private ProductWxGwRepository productWxGwRepository;
	@RequestMapping(value = "/wifi/control", method = RequestMethod.POST)
	public String ledControl(String key,Long deviceId,String tokenId,HttpServletRequest req){
		//判断该设备的产品是否与公众号关联绑定
		System.out.println("deviceId-------------:"+deviceId);
		System.out.println("tokenId--------------："+tokenId);
		/* 不需要在这里验证
		Device device = deviceRepository.findById(deviceId);
		GateWayWx wxgw = wxGateWayRepository.findByTokenId(tokenId);
		ProductGateWayWx prowx = productWxGwRepository.findByProductIdAndAppName(device.getPid(), wxgw.getAppName());
		if (prowx==null) {
			return "{\"status\":40001,\"msg\":productWx not bound !}";
		}
		*/
		String data=null;
		if ("off".equals(key)) {
			data="01";
		}
		if ("blue".equals(key)) {
			data="02";
		}
		if ("orange".equals(key)) {
			data="03";
		}
		if ("red".equals(key)) {
			data="04";
		}
		if ("white".equals(key)) {
			data="05";
		}
		if ("purple".equals(key)) {
			data="07";
		}
		if ("yellow".equals(key)) {
			data="08";
		}
		String rs;
		try {
			String body = "{\"device\":{\"id\":" + deviceId + ",\"data\":\""
					+ data + "\"}}";
			System.out.println(body);
			Map<String, String> map = boundInfoService
					.getCmServerByDeviceId(deviceId);
			String signed = MD5Util.MD5(body + map.get("key"));
			String ip = map.get("ip");
			System.out.println(map.get("webpt"));
			int webpt = Integer.parseInt(map.get("webpt"));
			String urlPost = "http://" + ip + ":" + webpt
					+ "/v1/device/pipe?signed=" + signed;
			rs = HttpUtil.executePost(urlPost, body);
			return rs;
		} catch (Exception e) {
			rs="{\"status\":40002,\"msg\":offline !}";
			return rs;
		}
	}
}
