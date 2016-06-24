package iotwechat.xlink.cloud;


 

import iotwechat.xlink.cloud.config.ConnectionSettings;
import iotwechat.xlink.cloud.config.RedisConSettings;
import iotwechat.xlink.cloud.config.WeChatSettings;
import iotwechat.xlink.cloud.domain.GateWayWx;
import iotwechat.xlink.cloud.redis.RedisBase;
import iotwechat.xlink.cloud.repository.WxGateWayRepository;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;


@EnableAspectJAutoProxy
@EnableConfigurationProperties({ConnectionSettings.class,RedisConSettings.class,WeChatSettings.class})
@Configuration
@EnableAutoConfiguration
@ComponentScan({"iotwechat.xlink.cloud"})
@PropertySources({
	@PropertySource(value = "classpath:application.properties"),
   @PropertySource(value = "file:${linux.config.path}", ignoreResourceNotFound = true),
   @PropertySource(value = "file:${window.config.path}", ignoreResourceNotFound = true),
   @PropertySource(value = "file:${osx.config.path}", ignoreResourceNotFound = true)
})
public class Application {

    public static void main(String[] args) {
    	
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        System.out.println("Let's inspect the beans provided by Spring Boot:..");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
        initData(ctx);
       
        System.out.println("ok,gatewayWx!!!!!!!!!!!!");
    }
    
    public static void initData(ApplicationContext ctx){
    	//初始化tokenMap
    	WxGateWayRepository wxGateWayRepository=(WxGateWayRepository)ctx.getBean("wxGateWayRepository");
    	List<GateWayWx> its=(List) wxGateWayRepository.findAll();
    	
    	RedisBase redisBase=(RedisBase)ctx.getBean("redisBase");
    	for(GateWayWx it:its){
    		redisBase.setHashSetValue(it.getTokenId(),"appId",it.getAppID());
    		redisBase.setHashSetValue(it.getTokenId(),"appsecret",it.getAppsecret());
    		redisBase.setHashSetValue(it.getTokenId(),"appName",it.getAppName());
    	}
    }
}
