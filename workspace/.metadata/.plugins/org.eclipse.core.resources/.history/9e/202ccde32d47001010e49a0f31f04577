package config;

import java.util.Properties;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration 
@ComponentScan(basePackages= {"controller","logic","service","dao","aop"})
@EnableAspectJAutoProxy //AOP 사용을 위한 설정
@EnableWebMvc  //기본 웹처리를 위한 설정. 
public class MvcConfig implements WebMvcConfigurer{
	// http://localhost:8080/shop1/item/list
	@Bean //객체화
	public HandlerMapping handlerMapping() { //url 처리.
		RequestMappingHandlerMapping hm = new RequestMappingHandlerMapping();
		hm.setOrder(0);
		return hm;
	}
	@Bean
	public ViewResolver viewResolver() { //뷰 결정자. 
		InternalResourceViewResolver vr = new InternalResourceViewResolver();
		vr.setPrefix("/WEB-INF/view/"); //  /WEB-INF/view/item/list.jsp
		vr.setSuffix(".jsp");
		return vr;
	}
	//파일 업로드를 위한 설정. enctype="multipart/form-data"요청 처리함. 
	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver mr = new CommonsMultipartResolver();
		mr.setMaxInMemorySize(1024 * 1024); //업로드시 메모리에서 처리가능 크기지정
		mr.setMaxUploadSize(1024 * 10240);  //업로드 가능한 파일의 크기 지정
		return mr;
	}
	//메세지를 코드값으로 처리하기 위한 설정
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
		ms.setBasename("messages"); // messages.properties로 설정
		ms.setDefaultEncoding("UTF-8");
		return ms;
	}
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
	@Override
	public void configureDefaultServletHandling
	(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}	
}
