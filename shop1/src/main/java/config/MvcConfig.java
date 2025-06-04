package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ComponentScan(basePackages= {"controller", "logic", "service", "dao"})
@EnableWebMvc // 기본 웹처리를 위한 설정

public class MvcConfig implements WebMvcConfigurer {

	@Bean // 객체화
	public HandlerMapping handlerMapping() { // url 처리(http://localhost:8080/item/list)
		RequestMappingHandlerMapping hm = new RequestMappingHandlerMapping();
		hm.setOrder(0);
		return hm;
	}
	
	@Bean // 객체화
	public ViewResolver viewResolver() { // 뷰 결정자.
		InternalResourceViewResolver vr = new InternalResourceViewResolver();
		vr.setPrefix("/WEB-INF/view/"); ///WEB-INF/view/ + item/list + .jsp
		vr.setSuffix(".jsp");
		return vr;
	}
	
	@Bean // 객체화(파일 업로드를 위한 설정. enctype="multipart/form-data" 요청 처리함)
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver mr = new CommonsMultipartResolver();
		mr.setMaxInMemorySize(1024 * 1024); // 업로드시 메모리에서 처리가능 크기지정
		mr.setMaxUploadSize(1024 * 10240);  // 업로드 가능한 파일의 크기 지정
		return mr;
	}
	
	@Override // 기본 웹파일 처리를 위한 설정 (이미지 보이게)
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	

}
