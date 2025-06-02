package main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import annotation.Article;
import annotation.Member;
import annotation.MemberService;
import annotation.ReadArticleService;
import annotation.UpdateInfo;
import config.AppCtx;
/*
1. 환경 설정 자바 클래스에 사용되는 어노테이션
@Configuration : 환경 설정 자바 클래스. xml 대체 되는 클래스
@CompenentScan : 객체 생성을 위한 패키지 설정.
@EnableAspectJAutoProxy : AOP를 사용하도록 설정
@Bean : 객체를 생성.

2. 클래스에서 사용되는 어노테이션
@Component : 객체화 되는 클래스. 객체 주입이 완료상태
@Autowired : 자료형 기준으로 객체 주입. 주입 대상의 객체가 없으면 오류 발생 [객체 주입 필수]
@Autowired(required=false) : 주입대상의 객체가 없으면 null로 주입함.  [객체 주입 선택]
@Scope : 일회용 객체로 생성. 사용될때 마다 새로운 객체로 생성함

3. AOP 관련 어노테이션
3-1) pointcut : 핵심메서드 설정
@Aspect : AOP로 사용될 클래스로 지정
@Order(순서) : 순서는 before 기준. after의 경우는 order의 역순으로 실행됨.


3-2) advice : 실행되는 시점 설정
@Before : 핵심기능/메서드 수행 이전
@AfterReturning : 핵심기능/메서드 정상 수행 이후. 리턴값 조회 가능
@AfterThrowing : 핵심기능/메서드 오류 수행 이후. 예외값 조회 가능
@After : 핵심기능/메서드 수행 이후
@Around : 핵심기능/메서드 수행 전,후 모두처리

*/
public class Main1 {
	public static void main(String[] args) {
		// ApplicationContext : 컨테이너. 객체들을 저장하고 있는 공간(2개 객체 존재) AOP 사용가능
		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(AppCtx.class);
		// 컨테이너에서 readArticleService 가져와서 ReadArticleService 형변환
		// service : ReadArticleServiceImpl 객체를 저장
		ReadArticleService service =
				ctx.getBean("readArticleService", ReadArticleService.class);
		try {
			// a1 : Article id값이 1인 객체
			Article a1 = service.getArticleAndReadCnt(1); // 핵심 메서드
			// a2 : Article id값이 1인 객체
			Article a2 = service.getArticleAndReadCnt(1);
			System.out.println("[main] a1 == a2 : " + (a1 == a2)); // false
			service.getArticleAndReadCnt(0);
		} catch (Exception e) {
			System.out.println("[main]" + e.getMessage());
		}
		
		System.out.println("\n UpdateTraceAspect 연습");
		MemberService ms = ctx.getBean("memberService", MemberService.class);
		ms.regist(new Member()); // 핵심기능, 핵심메서드
		ms.update("hong", new UpdateInfo());
		ms.delete("hong2", "test", new UpdateInfo());
	}
}
