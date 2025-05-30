package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration // spring 환경을 설정하는 클래스 
@ComponentScan(basePackages= {"annotation"})
// annotation 패키지에 속한 클래스 중 @Component 어노테이션을 가진 클래스의 객체를 생성 후 저장
@EnableAspectJAutoProxy
// AOP설정함. AOP 관련 어노테이션 기능 사용
public class AppCtx {
	
}
