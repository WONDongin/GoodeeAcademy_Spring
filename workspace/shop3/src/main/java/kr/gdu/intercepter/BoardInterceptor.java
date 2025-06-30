package kr.gdu.intercepter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.gdu.domain.User;
import kr.gdu.exception.ShopException;
import org.springframework.web.servlet.HandlerInterceptor;

public class BoardInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle  // /board/write 요청 시 Controller 보다 먼저 호출됨
	(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String boardid = request.getParameter("boardid");
		HttpSession session = request.getSession();
		User login = (User)session.getAttribute("loginUser");
		if(boardid == null || boardid.equals("1")) {
			if(login == null || !login.getUserid().equals("admin")) {
				String msg = "관리자만 거래 가능합니다.";
				String url = request.getContextPath()+ "/board/list?boardid=1";
				throw new ShopException(msg,url);
			}
		}
		return true; //정상적인 거래. BoardController의 메서드 실행
	}
}
