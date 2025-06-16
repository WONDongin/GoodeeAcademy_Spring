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

import kr.gdu.intercepter.BoardInterceptor;

@Configuration 
//@EnableAspectJAutoProxy //AOP 사용을 위한 설정
//@EnableWebMvc  //기본 웹처리를 위한 설정. 
//   http://localhost:8080/user/login
public class MvcConfig implements WebMvcConfigurer{
	//예외처리 객체
	@Bean
	public SimpleMappingExceptionResolver exceptionHandler() {
		SimpleMappingExceptionResolver ser = new SimpleMappingExceptionResolver();
		Properties pr = new Properties();
		pr.put("exception.ShopException", "exception");
		//exception.ShopException 예외 발생시 exception.jsp 페이지 이동
		ser.setExceptionMappings(pr);
		return ser;
	}
	//기본 웹파일 처리를 위한 설정
//	@Override
//	public void configureDefaultServletHandling
//	(DefaultServletHandlerConfigurer configurer) {
//		configurer.enable();
//	}	
	@Bean
    @Primary   //우선 적용
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {  //Connection 객체
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariDataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type
        		(HikariDataSource.class).build(); //Connection POOL객체
    }
	//인터셉터관련 설정
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new BoardInterceptor())
		.addPathPatterns("/board/write") //요청 url 정보
		.addPathPatterns("/board/update")
		.addPathPatterns("/board/delete");		
	}    
}
