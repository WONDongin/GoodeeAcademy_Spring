package kr.gdu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import kr.gdu.sitemesh.SiteMeshFilter;

@SpringBootApplication
@ServletComponentScan //하위 패키지모두가 Scan 대상
@EnableAspectJAutoProxy
public class Shop2Application { //springboot의 시작
	public static void main(String[] args) {
		SpringApplication.run(Shop2Application.class, args);
	}

	@Bean
	public FilterRegistrationBean<SiteMeshFilter> siteMeshFilter() {
	    FilterRegistrationBean<SiteMeshFilter> filter = 
				             new FilterRegistrationBean<SiteMeshFilter>();
		filter.setFilter(new SiteMeshFilter());
		return filter;
	 }
}
