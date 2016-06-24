package iotwechat.xlink.cloud.util;

import java.util.UUID;

public class UUID16 {
	  public static  String getUUID16(){
	    	String ab=UUID.randomUUID().toString().replace("-", "");
	    	ab=ab.substring(0,4)+ab.substring(10,14)+ab.substring(16, 20)+ab.substring(20, 24);
	    	return ab;
	    }

}
