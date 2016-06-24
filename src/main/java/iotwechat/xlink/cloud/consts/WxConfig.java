package iotwechat.xlink.cloud.consts;

/**
 * 微信公众账号开发者配置
 */
public abstract class WxConfig {

//	public static  String APPID="wxe6ed495161ac50dc";
//	public static  String APPSECRET="6cf4c228c844f7ea36b38fa738d00ba7";
	//在公众号平台上配置的URL验证的字符串
	public static  String TOKEN="wxgatewaytoken";

	//设备授权的属性值
	public static String Connect_protocol="3";//设备类型 android classic bluetooth – 1 ios classic bluetooth – 2 ble – 3 wifi -- 4
	// 不加密时 authKey 为空字符串，crypt_method、auth_ver都为0
	// 加密时 authKey 需为符合格式的值，crypt_method、auth_ver都为1
	public static String Auth_key="";	//加密key 1234567890ABCDEF1234567890ABCDEF
	public static String Crypt_method="0";    //auth加密方法  0：不加密 1：AES加密
	public static String Auth_ver="0";        //0：不加密的version 1：version 1
	public static String Conn_strategy="1";   //连接策略
	public static String Close_strategy="1";  //1：退出公众号页面时断开 2：退出公众号之后保持连接不断开 3：一直保持连接（设备主动断开连接后，微信尝试重连）

							// 低功耗蓝牙必须为-1
	public static String Manu_mac_pos="-1";   //表示mac地址在厂商广播manufature data里含有mac地址的偏移，取值如下： -1：在尾部、 -2：表示不包含mac地址
	public static String Ser_mac_pos="-1"; 
}
