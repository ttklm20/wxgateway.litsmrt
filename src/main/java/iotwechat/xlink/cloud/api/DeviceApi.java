package iotwechat.xlink.cloud.api;

import iotwechat.xlink.cloud.domain.DeviceAuth;
import iotwechat.xlink.cloud.util.HttpAccessTokenException;
import iotwechat.xlink.cloud.util.HttpUtil;

import java.util.List;

import net.sf.json.JSONObject;

import org.eclipse.jetty.util.ajax.JSON;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 设备相关 API
 * <p>
 * https://api.weixin.qq.com/device/ 下的API为设备相关API， 测试号可以调用，正式服务号需要申请权限后才能调用。
 */
public class DeviceApi {

	private static final String TransMsgUrl = "https://api.weixin.qq.com/device/transmsg?access_token=ACCESS_TOKEN";
	public static final String AuthorizeUrl = "https://api.weixin.qq.com/device/authorize_device?access_token=ACCESS_TOKEN";
	public static final String CreateQrcode = "https://api.weixin.qq.com/device/create_qrcode?access_token=ACCESS_TOKEN";
	private static final String GetStatUrl = "https://api.weixin.qq.com/device/get_stat?access_token=ACCESS_TOKEN&device_id=DEVICE_ID";
	private static final String VerifyQrcodeUrl = "https://api.weixin.qq.com/device/verify_qrcode?access_token=ACCESS_TOKEN";
	private static final String GetOpenidUrl = "https://api.weixin.qq.com/device/get_openid?access_token=ACCESS_TOKEN&device_type=DEVICE_TYPE&device_id=DEVICE_ID";
	//新授权接口：deviceId由微信平台产生，同时返回二维码ticket
	private static final String AuthorizeNewUrl="https://api.weixin.qq.com/device/getqrcode?access_token=ACCESS_TOKEN";
	private static final String deviceBindUrl="https://api.weixin.qq.com/device/bind?access_token=ACCESS_TOKEN";
	private static final String getBindDeviceUrl="https://api.weixin.qq.com/device/get_bind_device?access_token=ACCESS_TOKEN&openid=OPENID";
	//系统强制解绑用户和设备
	private static final String compelBind="https://api.weixin.qq.com/device/compel_bind?access_token=ACCESS_TOKEN";
	private static final String compelUnbind="https://api.weixin.qq.com/device/compel_unbind?access_token=ACCESS_TOKEN";
//	/**
//	 * 向设备推送消息
//	 */
//	public static String transMsg(String deviceType, String deviceID,
//			String openID, String content) {
//		JSONObject json=new JSONObject();
//		json.put("device_type", deviceType);
//		json.put("device_id", deviceID);
//		json.put("open_id", openID);
//		json.put("content", content);
//		String body = json.toString();
//		System.out.println("transMsg body=" + body);
//		String ret = HttpUtil.doPost(TransMsgUrl, body);
//		System.out.println("transMsg ret=" + ret);
//		return ret;
//	}
//
	/**
	 * 根据设备id获取二维码生成串
	 * @throws Exception 
	 */
	public static String createQrcode(List<String> deviceIds,String tokenId, String accessToken) throws HttpAccessTokenException {
		JSONObject json = new JSONObject();
		json.put("device_num", deviceIds.size());
		json.put("device_id_list", deviceIds);
		return HttpUtil.doPost(CreateQrcode, json.toString(),tokenId, accessToken);
	}

	/**
	 * 批量授权/更新设备属性
	 * <p>
	 * 授权后设备才能进行绑定操作
	 * 
	 * @param devices
	 *            设备属性列表
	 * @param isCreate
	 *            是否首次授权： true 首次授权； false 更新设备属性
	 * @throws Exception 
	 */
	public static String authorize(List<DeviceAuth> devices, boolean isCreate,String tokenId, String accessToken) throws HttpAccessTokenException {
		JSONObject json = new JSONObject();
		json.put("device_num", String.valueOf(devices.size()));
		json.put("op_type", isCreate ? "0" : "1");// 请求操作的类型 0：设备授权（缺省值为0）
													// 1：设备更新（更新已授权设备的各属性值）
		json.put("device_list", devices);
		return HttpUtil.doPost(AuthorizeUrl, json.toString(),tokenId, accessToken);
	}
//	/**
//	 * 新授权接口，由微信公众平台生成deviceID
//	 * @param deviceId
//	 * @return
//	 */
//	public static String authorizeNew() {
//		return HttpUtil.doGet(AuthorizeNewUrl);
//	}
//	
//	public static String device_Bind(String deviceId, String ticket,String openId) {
//		JSONObject json = new JSONObject();
//		json.put("ticket", deviceId);
//		json.put("device_id", ticket);// 请求操作的类型 0：设备授权（缺省值为0）
//		// 1：设备更新（更新已授权设备的各属性值）
//		json.put("openid", openId);
//		return HttpUtil.doPost(deviceBindUrl, json.toString());
//	}
//	/**
//	 * 第三方强制解绑设备和用户
//	 * @param deviceId
//	 * @param ticket
//	 * @param openId
//	 * @return
//	 */
//	public static String compel_unbind(String deviceId,String openId) {
//		JSONObject json = new JSONObject();
//		json.put("device_id", deviceId);
//		json.put("openid", openId);
//		return HttpUtil.doPost(compelUnbind, json.toString());
//	}
//	/**
//	 * 第三方强制绑定设备和用户
//	 * @param deviceId
//	 * @param openId
//	 * @return
//	 */
//	public static String compel_bind(String deviceId,String openId) {
//		JSONObject json = new JSONObject();
//		json.put("device_id", deviceId);
//		json.put("openid", openId);
//		return HttpUtil.doPost(compelBind, json.toString());
//	}
//
//	/**
//	 * 设备状态查询
//	 * <p>
//	 * status 0：未授权 1：已经授权（尚未被用户绑定） 2：已经被用户绑定<br/>
//	 * {"errcode":0,"errmsg":"ok","status":1,"status_info":"authorized"}
//	 */
//	public static String getStat(String deviceId) {
//		String url = GetStatUrl.replace("DEVICE_ID", deviceId);
//		return HttpUtil.doGet(url);
//	}
//	/**
//	 * 根据openId获取deviceId
//	 * @param deviceId
//	 * @return
//	 */
//	public static String getBindDevice(String openId) {
//		String url = getBindDeviceUrl.replace("OPENID", openId);
//		return HttpUtil.doGet(url);
//	}
//
//	/**
//	 * 验证二维码 获取二维码对应设备属性
//	 * 
//	 * @param ticket
//	 *            二维码生成串
//	 */
//	public static String verifyQrcode(String ticket) {
//		JSONObject json = new JSONObject();
//		json.put("ticket", ticket);
//		return HttpUtil.doPost(VerifyQrcodeUrl, json.toString());
//	}
//
//	/**
//	 * DEVICE_TYPE :公众账号原始ID
//	 * 根据设备类型和设备id查询绑定的openid
//	 */
//	public static String getOpenId(String deviceType, String deviceId) {
//
//		String url = GetOpenidUrl.replace("DEVICE_TYPE", deviceType).replace(
//				"DEVICE_ID", deviceId);
//		return HttpUtil.doGet(url);
//	}
}
