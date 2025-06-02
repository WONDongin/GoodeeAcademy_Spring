package annotation;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component // 객체화
@Aspect    // AOP 처리 클래스
@Order(3)  // AOP 처리 클래스의 순서 지정
public class LoggingAspect {
	/*
	[AOP 관련 용어]
	1) pointcut : 핵심메서드 설정
	execution(public * annotation..*(..))
	=> annotation 패키지에 속한 모든 public 메서드를 핵심 메서드로 설정!

	execution(접근제한자 * annotation..*(..))
	* : 리턴 타입과 상관 없음
	annotation..* : annotation 패키지의 모든 클래스(클래스명 상관x)
	(..) : 매개변수와 상관없음
	
	2) advice : 실행되는 시점 설정
	before : 핵심 메서드 실행 전
	after : 핵심 메서드 실행 후
	afterReturning : 핵심 메서드 정상 실행 후(오류없이)
	afterThrowing : 핵심 메서드 오류 실행 후
	around : 핵심 메서드 실행 전,후
	
	3) aspect : AOP 처리 클래스
	*/
	
	/*
	[LA]Before 메서드 실행 전 호출 ===> AOP 클래스(LoggingAspect)의 before 메서드
	getArticleAndReadCnt(1) ===> 핵심기능, 핵심메서드
	[LA]AfterReturning 메서드 정상 종료후 호출, 리턴값=annotation.Article@58ebfd03
	[LA] After 메서드 종료 후 실행
	
	[LA]Before 메서드 실행 전 호출
	getArticleAndReadCnt(1)
	[LA]AfterReturning 메서드 정상 종료후 호출, 리턴값=annotation.Article@5b07730f
	[LA] After 메서드 종료 후 실행
	
	[main] a1 == a2 : false
	
	[LA]Before 메서드 실행 전 호출
	getArticleAndReadCnt(0)
	[LA]AfterThrowing 메서드 예외 종료후 호출, 예외메시지= id는 0이 안됨
	[LA] After 메서드 종료 후 실행
	
	[main] id는 0이 안됨 
	*/
	
	// pointcut 설정
	final String publicMethod = "execution(public * annotation..*(..))";
	// advice 설정
	// annotation 패키기의 모든 클래스의 public 메서드의 호출 전
	@Before(publicMethod)
	public void before() {
		System.out.println("[LA] Before 메서드 실행 전 호출");
	}
	
	@AfterReturning(pointcut=publicMethod,returning="ret")
	public void afterReturning(Object ret) {
		System.out.println
		("[LA] AfterReturning 메서드 정상 종료후 호출, 리턴값="+ret);
	}
	
	@AfterThrowing(pointcut=publicMethod,throwing="ex")
	public void afterThrowing(Throwable ex) {
		System.out.println
		("[LA] AfterThrowing 메서드 예외 종료후 호출, 예외메시지="+ex.getMessage());
	}
	
	@After(publicMethod)
	public void afterFinally() {
		System.out.println("[LA] After 메서드 종료 후 실행");
	}
}