package kr.gdu.aop;

import jakarta.servlet.http.HttpSession;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import kr.gdu.domain.User;
import kr.gdu.dto.CartDto;
import kr.gdu.dto.UserDto;
import kr.gdu.exception.ShopException;

@Component
@Aspect
public class CartAspect {
	/*
	 * pointcut : CartController 클래스의 매개변수의 마지막이 HttpSession 인,
	 *            check* 로 시작하는 메서드
	 * advice : Before로 설정함           
	 */
	@Before("execution(* kr.gdu.controller.Cart*.check*(..)) && args(..,session)")
	public void cartCheck(HttpSession session) throws Throwable {
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			throw new ShopException
			("회원만 주문이 가능합니다. 로그인 하세요","../user/login");
		}
		CartDto cart =(CartDto)session.getAttribute("CART");
		if(cart == null || cart.getItemSetList().size() == 0) {
//		if(cart.getItemSetList().size() == 0 || cart == null) {
			throw new ShopException("장바구니에 상품을 추가하세요",
					"../item/list");
		}
	}
}
