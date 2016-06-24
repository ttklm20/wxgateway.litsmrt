package iotwechat.xlink.cloud.rest;

import iotwechat.xlink.cloud.util.ResponseCommonCode;

import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseRestController {
	public Object parseBody(String body){
		
		ObjectMapper mapper = new ObjectMapper();  
		JsonNode rootNode=null;
		HashMap<Object, Object> returnMap = new HashMap<Object, Object>();
        try {
			 rootNode = mapper.readTree(body);
		} catch (JsonProcessingException e) {
			 returnMap.put("status",ResponseCommonCode._400[0]);
			 returnMap.put("msg",ResponseCommonCode._400[1]);
			 return returnMap; 
		} catch (IOException e) {
			 returnMap.put("status",ResponseCommonCode._500[0]);
			 returnMap.put("msg",ResponseCommonCode._500[1]);
			 return returnMap; 
		}
		return rootNode;
	}
}
