package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import exception.ShopException;
import logic.User;

/*
1. pointcut : UserControlle.idCheck* 메서드로 시작하고, 마지막 매개변수가 String(userid), HttpSession 인 메서드를 설정
2. advice : Around로 이용
*/
@Component
@Aspect
public class UserLoginAspect {
	@Around("execution(* controller.User*.idCheck*(..)) && args(..,userid,session)")
	public Object userIdCheck(ProceedingJoinPoint joinPoint, String userid, HttpSession session) throws Throwable{
		User loginUser = (User) session.getAttribute("loginUser");
		if(loginUser == null || !(loginUser instanceof User)) { // 로그아웃 상태, 유저객체가 아닌경우 발동
			throw new ShopException("[idCheck] 로그인이 필요합니다.", "login");
		}
		if(!loginUser.getUserid().equals("admin") && !loginUser.getUserid().equals(userid)) { // 다른 사람 정보조회
			throw new ShopException("[idCheck] 본인 정보만 거래 가능합니다.", "../item/list");
		}
		return joinPoint.proceed();
	}
	
	@Around("execution(* controller.User*.loginCheck*(..)) && args(..,session)")
	public Object loginCheck(ProceedingJoinPoint joinPoint, HttpSession session) throws Throwable{
		User loginUser = (User) session.getAttribute("loginUser");
		if(loginUser == null || !(loginUser instanceof User)) { // 로그아웃 상태
			throw new ShopException("[loginCheck] 로그인이 필요합니다.", "login");
		}
		return joinPoint.proceed();
	} 
	
}
