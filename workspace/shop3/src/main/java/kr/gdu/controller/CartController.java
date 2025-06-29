package kr.gdu.controller;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.gdu.domain.Item;
import kr.gdu.domain.Sale;
import kr.gdu.domain.User;
import kr.gdu.dto.CartDto;
import kr.gdu.dto.ItemDto;
import kr.gdu.dto.ItemSetDto;
import kr.gdu.dto.UserDto;
import kr.gdu.service.ShopService;
import kr.gdu.util.CipherUtil;
@Controller
@RequestMapping("cart")
public class CartController {
	@Autowired
	private ShopService service;
	/*
	 * 문제
	 * 1.  
	 * 장바구니에 존재하는 상품의 경우 수량만 증가하기
	 * 장바구니에 존재하는 상품이 아닌 경우 상품 추가하기
	 * 
	 * 2. 
	 * 비밀번호 찾기를 비밀번호 초기화로 수정하기
	 *  기존 비밀번호 : 1234
	 *  비밀번호 초기화 : 전체 6자리의 대문자/소문자/숫자 임의의 조합으로 변경하기
	 *                 사용자에게 출력하기
	 */
	@RequestMapping("cartAdd")
	public ModelAndView add(Integer id,Integer quantity,HttpSession session){
		//new ModelAndView(뷰명) : /WEB-INF/view/cart/cart.jsp
		ModelAndView mav = new ModelAndView("cart/cart");
		Item item = service.getItem(id);  //id의 해당하는 Item 객체
		CartDto cart = (CartDto)session.getAttribute("CART");
		if(cart == null) { //session의 CART이름의 객체가 없는 경우
			cart = new CartDto();
			session.setAttribute("CART", cart);
		}
		cart.push(new ItemSetDto(item,quantity));
		mav.addObject("message",item.getName()+":"+quantity+"개 장바구니 추가");
		mav.addObject("cart",cart);
		return mav;
	}
	@RequestMapping("cartDelete")
	public ModelAndView delete(int index,HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		CartDto cart = (CartDto)session.getAttribute("CART");
		ItemSetDto removeObj = cart.getItemSetList().remove(index);
		/*
		 * E remove(int)  :  인덱스에 해당하는 객체를 제거. 제거된 객체를 리턴
		 * boolean remove(Object) : 객체를 입력받아서 객체를 제거. 제거여부를 리턴
		 */
		mav.addObject("message",removeObj.getItem().getName() 
				+ "가(이) 삭제 되었습니다.");
		mav.addObject("cart",cart);
		return mav;
	}
	@RequestMapping("cartView")
	public ModelAndView view(HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		mav.addObject("message","장바구니 상품 조회");
		mav.addObject("cart",session.getAttribute("CART"));		
		return mav;
	}
	/*
	 * 주문전 확인 페이지
	 * 1. 장바구니에 상품 존재해야함
	 *    상품이 없는경우 예외 발생. 
	 * 2. 로그인 된 상태여야함
	 *    로그아웃상태 : 예외 발생   
	 */
	@RequestMapping("checkout")
	public String checkout(Model model,HttpSession session) {
		User user = (User)session.getAttribute("loginUser");
		String hashuser = CipherUtil.makehash(user.getUserid());
		String plainEmail = CipherUtil.decrypt(user.getEmail(), hashuser);
		model.addAttribute("email",plainEmail);
		return null;
	}
	@RequestMapping("end")
	public ModelAndView checkend(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		CartDto cart = (CartDto)session.getAttribute("CART"); //장바구니 상품
		User loginUser = (User)session.getAttribute("loginUser"); //로그인 정보
		Sale sale = service.checkend(loginUser,cart);
		session.removeAttribute("CART"); //장바구니 제거
		mav.addObject("sale",sale);
		return mav;
	}
	
}
