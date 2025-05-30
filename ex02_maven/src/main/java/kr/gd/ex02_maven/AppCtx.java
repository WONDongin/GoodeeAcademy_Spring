package kr.gd.ex02_maven;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration // spring 환경을 설정 기능의 프로그램
@ComponentScan(basePackages = {"kr.gd.ex02_maven"}) // 패기지 안 파일 훓기
public class AppCtx {
	
}
