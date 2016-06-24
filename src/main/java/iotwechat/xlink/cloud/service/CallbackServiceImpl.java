package iotwechat.xlink.cloud.service;

import iotwechat.xlink.cloud.api.DeviceApi;
import iotwechat.xlink.cloud.api.MpApi;
import iotwechat.xlink.cloud.consts.MsgType;
import iotwechat.xlink.cloud.consts.XmlResp;
import iotwechat.xlink.cloud.domain.BoundInfo;
import iotwechat.xlink.cloud.mock.DBMock;
import iotwechat.xlink.cloud.protocal.BlueLight;
import iotwechat.xlink.cloud.protocal.BlueLight.CmdId;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 回调业务处理
 */
@Service
@Validated
public class CallbackServiceImpl implements CallbackService {
	
	@Inject
	private BoundInfoService boundInfoService;
	// 自定义菜单中的key值
	public static final String V1001_LIGHT_ON = "V1001_LIGHT_ON";//点灯
	public static final String V1002_LIGHT_OFF = "V1002_LIGHT_OFF";//灭灯
	
	// 针对不同类型的消息和事件进行处理
	public String handle(Map<String, String> reqMap) throws Exception {
		String msgType = reqMap.get("MsgType");
		String fromUser = reqMap.get("FromUserName"); 
		String toUser = reqMap.get("ToUserName");// deviceType


		// 文本消息
		if (MsgType.TEXT.equals(msgType)) {
			// 可以在此处进行关键字自动回复
			String content = "收到文本消息：" + reqMap.get("Content");
			return XmlResp.buildText(fromUser, toUser, content);
		}
		if (MsgType.IMAGE.equals(msgType)) {
			
		}
		// 基础事件推送，关注公众号和取消关注、菜单点击事件
		if (MsgType.EVENT.equals(msgType)) {
			String event = reqMap.get("Event");
			// 关注公众号
			if (MsgType.Event.SUBSCRIBE.equals(event)) {
				// 回复欢迎语
				return XmlResp.buildText(fromUser, toUser, "欢迎关注小图公众号！");
			}
		}
		return "";
	}
}
