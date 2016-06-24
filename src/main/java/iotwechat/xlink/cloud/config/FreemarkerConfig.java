package iotwechat.xlink.cloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configuration
public class FreemarkerConfig {

	//@Value("${spring.view.prefix}")
	//private String viewPrefix;

	@Value("${spring.view.suffix}")
	private String viewSuffix;
	
	@Value("${spring.view.cache}")
	private boolean cache;

	@Bean
	public FreeMarkerConfig freemarkerConfig() {
		
		FreeMarkerConfig configurer = new FreeMarkerConfigurer();

		configurer.getConfiguration().setDefaultEncoding("UTF-8");
		//configurer.getConfiguration().setTemplateLoader(loader);
		
		return configurer;
	}


	@Bean
	public ViewResolver freeMarkerViewResolver() {
		
		FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();
		viewResolver.setCache(cache);
		viewResolver.setPrefix("");
		viewResolver.setSuffix(viewSuffix);
		viewResolver.setContentType("text/html;charset=UTF-8");
		viewResolver.setExposeSpringMacroHelpers(true);
		viewResolver.setExposeRequestAttributes(false);
		viewResolver.setExposeSessionAttributes(false);
		
		
		return viewResolver;
	}
}
