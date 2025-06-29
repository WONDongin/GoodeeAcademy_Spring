package kr.gdu.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpSession;
import kr.gdu.domain.User;
import kr.gdu.dto.UserDto;
import kr.gdu.exception.ShopException;

@Component
@Aspect
public class AdminLoginAspect {
	@Around("execution(* kr.gdu.controller.AdminController.*(..)) && args(..,session)")
	public Object adminCheck(ProceedingJoinPoint joinPoint, HttpSession session) 
			throws Throwable {
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null || !(loginUser instanceof User)) {
			throw new ShopException("[adminCheck]로그인 하세요","../user/login");
		}
		else if (!loginUser.getUserid().equals("admin")) {
			throw new ShopException
			("[adminCheck]관리자만 가능한 거래 입니다","../user/mypage?userid="+loginUser.getUserid());
		}
		return joinPoint.proceed();
	}
}
