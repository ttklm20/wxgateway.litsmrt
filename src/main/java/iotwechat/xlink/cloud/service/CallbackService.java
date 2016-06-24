package iotwechat.xlink.cloud.service;

import java.util.Map;

public interface CallbackService {
	public String handle(Map<String, String> reqMap) throws Exception;
}
