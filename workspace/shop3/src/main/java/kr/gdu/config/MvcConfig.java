package kr.gdu.config;

import java.util.Properties;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.zaxxer.hikari.HikariDataSource;

//import kr.gdu.intercepter.BoardInterceptor;

@Configuration
public class MvcConfig implements WebMvcConfigurer{
	//예외처리 객체
	@Bean
	public SimpleMappingExceptionResolver exceptionHandler() {
		SimpleMappingExceptionResolver ser = new SimpleMappingExceptionResolver();
		Properties pr = new Properties();
		pr.put("exception.ShopException", "exception");
		//exception.ShopException 예외 발생시 exception.html 페이지 이동
		ser.setExceptionMappings(pr);
		return ser;
	}
	//인터셉터관련 설정
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(new BoardInterceptor())
//		.addPathPatterns("/board/write") //요청 url 정보
//		.addPathPatterns("/board/update")
//		.addPathPatterns("/board/delete");		
//	}    
}
