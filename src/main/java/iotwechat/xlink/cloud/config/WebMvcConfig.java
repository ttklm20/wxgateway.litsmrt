package iotwechat.xlink.cloud.config;

import iotwechat.xlink.cloud.intercepter.LoginIntercepter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	/**
	 * 加载拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		LoginIntercepter loginIntercepter = new LoginIntercepter();
		registry.addInterceptor(loginIntercepter)
			.addPathPatterns("/**");
//			.excludePathPatterns("/v1/**","/login.html","/register.html","/css/**","/img/**","/js/**");
	}
}
