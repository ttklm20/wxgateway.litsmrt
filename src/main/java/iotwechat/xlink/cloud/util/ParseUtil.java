package iotwechat.xlink.cloud.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class ParseUtil {
	/**
	 * 流转换为字符串
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {      
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));      
        StringBuilder sb = new StringBuilder();      
        String line = null;
       try {      
           while ((line = reader.readLine()) != null) {      
                sb.append(line + "\n");      
            }      
        } catch (IOException e) {      
            e.printStackTrace();      
        } finally {      
           try {      
                is.close();      
            } catch (IOException e) {      
                e.printStackTrace();      
            }      
        }      
       return sb.toString();      
    }
	/**
	 * 将json字符串转化为jsonNode
	 * @param json
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public static JsonNode parseStringToJsonNode(String json) throws JsonProcessingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(json);
		return jsonNode;
	}
	/**
	 * 将Java对象转化为JsonNode
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public static JsonNode parseToJsonNode(Object object) throws IOException{
		ObjectMapper mapper=new ObjectMapper();
		String objectJson = mapper.writeValueAsString(object);
		JsonNode objectJsonNode = mapper.readTree(objectJson);
		return objectJsonNode;
	}
	
	public static String getQrImageFilePath(String path, String mac, String deviceId, String format) {
		String fileName = path +mac+"_"+ deviceId + "."+ format;
		return fileName;
	}
	
	/**
	 * 使用zxing库生成二维码图片
	 */
	public static String createQrImage(String path,String mac, String deviceId,
			String ticket) {
		path = path.endsWith("/") ? path : path + "/";
		int width = 430;
		int height = 430;
		// 二维码的图片格式
		String format = "jpg";
		String fileName = getQrImageFilePath(path, mac, deviceId, format);
		// 设置二维码的参数
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//编码
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);//容错率
		try {
			// 生成二维码
			BitMatrix bitMatrix = new MultiFormatWriter().encode(ticket,
					BarcodeFormat.QR_CODE, width, height, hints);
			// 输出图片
			File outputFile = new File(fileName);
			MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);

			System.out.println("设备id：" + deviceId + "，ticket："+  ticket + "，生成二维码图片：" + fileName);
			
			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
}
