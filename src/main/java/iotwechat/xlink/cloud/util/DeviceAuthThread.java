package iotwechat.xlink.cloud.util;

import iotwechat.xlink.cloud.api.DeviceApi;
import iotwechat.xlink.cloud.consts.WxConfig;
import iotwechat.xlink.cloud.domain.AccessToken;
import iotwechat.xlink.cloud.domain.Device;
import iotwechat.xlink.cloud.domain.DeviceAuth;
import iotwechat.xlink.cloud.domain.Product;
import iotwechat.xlink.cloud.domain.ProductGateWayWx;
import iotwechat.xlink.cloud.repository.DeviceRepository;
import iotwechat.xlink.cloud.repository.WxGateWayRepository;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DeviceAuthThread extends Thread {

	private List<Device> devices;
	private String tokenId;
	private String imagePath;
	private DeviceRepository deviceRepository;
	private AccessTokenHelper accessTokenHelper;
	private boolean newDevice = false;
	private Product product;
	private ProductGateWayWx productGateWayWx;

	public DeviceAuthThread(List<Device> devices, String tokenId,
			String imagePath, DeviceRepository deviceRepository, AccessTokenHelper accessTokenHelper, boolean newDevice, Product product, ProductGateWayWx productGateWayWx) {
		this.devices = devices;
		this.tokenId = tokenId;
		this.imagePath = imagePath;
		this.deviceRepository = deviceRepository;
		this.productGateWayWx = productGateWayWx;
		this.accessTokenHelper = accessTokenHelper;
		this.newDevice = newDevice;
		this.product = product;
	}

	public void run() {
		try {
			ArrayList<DeviceAuth> auths = new ArrayList<DeviceAuth>();
			Device updateDevice;
			for (int i = 0; i < devices.size(); i++) {
				updateDevice = devices.get(i);
				DeviceAuth device = new DeviceAuth();
				device.setId(devices.get(i).getId().toString());
				device.setMac(devices.get(i).getMacAddress());
				// 设备类型 android classic
				// bluetooth – 1 ios classic
				// bluetooth – 2 ble – 3 wifi --
				// 4
				if( product.getType() == 3 ) {
					device.setConnect_protocol("3");
				} else {
					device.setConnect_protocol("4");
				}
				
				// 不加密时 authKey 为空字符串，crypt_method、auth_ver都为0
				// 加密时 authKey 需为符合格式的值，crypt_method、auth_ver都为1
				device.setAuth_key(productGateWayWx.getAuthKey()); // 加密key
														// 1234567890ABCDEF1234567890ABCDEF
				device.setCrypt_method(productGateWayWx.getCryptMethod()); // auth加密方法 0：不加密
																// 1：AES加密
				device.setAuth_ver(productGateWayWx.getAuthVer()); // 0：不加密的version
														// 1：version 1
				device.setConn_strategy(productGateWayWx.getConnStrategy()); // 连接策略
				device.setClose_strategy(productGateWayWx.getCloseStrategy()); // 1：退出公众号页面时断开
																	// 2：退出公众号之后保持连接不断开
																	// 3：一直保持连接（设备主动断开连接后，微信尝试重连）

				// 低功耗蓝牙必须为-1
				device.setManu_mac_pos(productGateWayWx.getManuMacPos()); // 表示mac地址在厂商广播manufature
																// data里含有mac地址的偏移，取值如下：
																// -1：在尾部、
																// -2：表示不包含mac地址
				//
				device.setSer_mac_pos(productGateWayWx.getSerMacPos());
				auths.add(device);

				// 到最后了，一起提交
				if (i == devices.size() - 1) {
					authorizeDevices(auths, newDevice, tokenId);
					auths.clear();
				}
				// 其他情况，每5个提交一次
				else if (auths.size() == 5) {
					authorizeDevices(auths, newDevice, tokenId);
					auths.clear();
				}

				// 初始化的token为null，初始化为空
				if (updateDevice.getWxTokenIds() == null) {
					updateDevice.setWxTokenIds("");
				}
				if (newDevice) {
					updateDevice.setWxTokenIds(updateDevice.getWxTokenIds()
							+ tokenId + ",");
				}
			}
			deviceRepository.save(devices);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.interrupt();
		}

	}

	/**
	 * 认证设备
	 * @param auths
	 * @param b
	 * @param tokenId2
	 */
	private void authorizeDevices(ArrayList<DeviceAuth> auths, boolean newDevice,
			String tokenId) {
		int tryCount = 0;
		while (tryCount < 2) {
			tryCount++;
			AccessToken accessToken = accessTokenHelper
					.getAccessTokenWithTokenId(tokenId);
			try {
				DeviceApi.authorize(auths, newDevice, tokenId,
						accessToken.getAccess_token());
				createQrByDeviceId(auths, imagePath);
				break;
			} catch (HttpAccessTokenException e) {
				e.printStackTrace();
				accessTokenHelper.clearAccessTokenWithTokenId(tokenId);
				continue;
			} catch (ConnectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
		}
	}

	/**
	 * 设备生成二维码
	 * 
	 * @param auths
	 * @throws ConnectException
	 *             生成二维码失败
	 */
	public void createQrByDeviceId(List<DeviceAuth> auths, String imagePath)
			throws ConnectException {
		List<String> list = new ArrayList<String>(); // 存储设备Id，批量推送设备Id生成二维码
		HashMap<String, String> macMap = new HashMap<String, String>();// 保存设备ID和MAC的关系
		for (DeviceAuth device : auths) {
			list.add(device.getId());
			macMap.put(device.getId(), device.getMac());
		}
		int tryCount = 0;
		String rs = "{}";
		// 用一个while循环完成access_token的更新
		while (tryCount < 2) {
			try {
				AccessToken accessToken = accessTokenHelper
						.getAccessTokenWithTokenId(tokenId);
				rs = DeviceApi.createQrcode(list, tokenId,
						accessToken.getAccess_token());// 生成二维码字符串
				break;
			} catch (HttpAccessTokenException e) {
				// 如果Token不对，清理redis的token，触发他再去走一边这个流程
				accessTokenHelper.clearAccessTokenWithTokenId(this.tokenId);
				// 但是最多只能刷一次token
				tryCount++;
			}
		}
		
		JSONObject rsJson = JSONObject.fromObject(rs);// 解析生成的二维码字符串
		if (rsJson.getInt("errcode") == 0) {// errCode==0说明执行成功
			JSONArray qrArr = (JSONArray) rsJson.get("code_list");
			for (Object qr : qrArr) {
				JSONObject jso = JSONObject.fromObject(qr);
				ParseUtil.createQrImage(imagePath,
						macMap.get(jso.getString("device_id")),
						jso.getString("device_id"), jso.getString("ticket"));
			}
		} else {
			throw new ConnectException();
		}

	}
}
