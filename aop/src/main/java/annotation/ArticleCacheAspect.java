package annotation;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(2)
/*
[ACA]getArticleAndReadCnt(1) 메서드 호출전
[LA] Before 메서드 실행 전 호출
getArticleAndReadCnt(1) == 핵심기능/메서드
[LA] AfterReturning 메서드 정상 종료후 호출, 리턴값=annotation.Article@4fc5e095
[LA] After 메서드 종료 후 실행
[ACA]getArticleAndReadCnt(1) 메서드 호출 후
[ACA] cache에서 Article[1] 추가함

[ACA]getArticleAndReadCnt(1) 메서드 호출전
[ACA] cache에서 Article[1] 가져옴

[main] a1 == a2 : true

[ACA]getArticleAndReadCnt(0) 메서드 호출전
[LA] Before 메서드 실행 전 호출
getArticleAndReadCnt(0)
[LA] AfterThrowing 메서드 예외 종료후 호출, 예외메시지= id는 0이 안됨
[LA] After 메서드 종료 후 실행

[main] id는 0이 안됨
*/
public class ArticleCacheAspect {
	private Map<Integer, Article> cache = new HashMap<>();
	// pointcut : 
	// *.. : 패키지에 상관없이
	// ReadArticleService : 클래스 명이 ReadArticleService인 경우
	@Around("execution(public * *..ReadArticleService.*(..))")
	// 예외처리 발생시 이후 작업작동x
	public Object cache(ProceedingJoinPoint joinPoint) throws Throwable{
		// getArgs() : 핵심기능의 매개변수값들
		Integer id = (Integer) joinPoint.getArgs()[0];
		System.out.println("[ACA]" + joinPoint.getSignature().getName() + "(" + id +  ") 메서드 호출전");
		// joinPoint.getSignature().getName() : 핵심메서드의 메서드 이름
		Article article = cache.get(id);
		if(article != null) {
			System.out.println("[ACA] cache에서 Article[" + id + "] 가져옴");
			return article;
		}
		
		// ret : 핵심 메서드의 리턴값
		Object ret = joinPoint.proceed(); // 다음 메서드 호출
		System.out.println("[ACA]" + joinPoint.getSignature().getName() + "(" + id + ") 메서드 호출 후");
		if(ret != null && ret instanceof Article) {
			cache.put(id, (Article)ret); // 1, Article(1)객체 저장
			System.out.println("[ACA] cache에서 Article[" + id + "] 추가함");
			
		}
		return ret;
	}
}
