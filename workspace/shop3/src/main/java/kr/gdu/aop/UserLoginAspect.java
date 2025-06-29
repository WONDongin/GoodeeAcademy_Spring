package kr.gdu.aop;

import jakarta.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import kr.gdu.exception.ShopException;
import kr.gdu.domain.User;
/*
 * 1. pointcut : UserController.idCheck* 메서드로 시작하고,
 *               마지막 매개변수가 String,HttpSession인 메서드를 설정 
 * 2. advice : Around로 이용              
 */
@Component
@Aspect
public class UserLoginAspect {
	@Around
  ("execution(* kr.gdu.controller.User*.idCheck*(..)) && args(..,userid,session)")
		public Object userIdCheck
		       (ProceedingJoinPoint joinPoint,String userid,
				HttpSession session) throws Throwable {
		   User loginUser = (User)session.getAttribute("loginUser");	
		   if(loginUser == null || !(loginUser instanceof User)) { //로그아웃상태
			   throw new ShopException("[idCheck]로그인이 필요합니다.","login");
		   }
		   if(!loginUser.getUserid().equals("admin") 
				   && !loginUser.getUserid().equals(userid)) {
			   throw new ShopException
			       ("[idCheck]본인 정보만 거래 가능합니다.","../item/list");
		   }
		   return joinPoint.proceed();	
		}
	@Around
("execution(* kr.gdu.controller.User*.loginCheck*(..)) && args(..,session)")
	public Object loginCheck(ProceedingJoinPoint joinPoint,
			HttpSession session) throws Throwable {
	   User loginUser = (User)session.getAttribute("loginUser");	
	   if(loginUser == null || !(loginUser instanceof User)) { //로그아웃상태
		   throw new ShopException("[loginCheck]로그인이 필요합니다.","login");
	   }
	   return joinPoint.proceed();	
	}
	
}
