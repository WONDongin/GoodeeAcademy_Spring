package kr.gdu.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.gdu.logic.Item;
import kr.gdu.service.ShopService;

@Controller   //@Component + Controller 기능. 객체화됨
@RequestMapping("item")  
public class ItemController {
	@Autowired  //ShopService 객체 주입
	private ShopService service;
	
	@RequestMapping("list") 
	public ModelAndView list() {
		//ModelAndView : View에 이름과, 전달 데이터를 저장
		ModelAndView mav = new ModelAndView();
		List<Item> itemList = service.itemList();
		mav.addObject("itemList",itemList);
		return mav;
	}
	
	@GetMapping({"detail","update","delete"}) //Get 방식 요청시 호출
	//Integer id : id 파라미터값을 저장. 매개변수명과 파라미터 이름이 같아야 함.  
	public ModelAndView detail(Integer id) { 
		ModelAndView mav = new ModelAndView();
		Item item = service.getItem(id);
		mav.addObject("item",item);
		return mav;
	}
	
	@GetMapping("create")  //Get 방식 요청
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new Item());
		return mav;
	}
	
	@PostMapping("create") //Post 방식 요청
	public ModelAndView register(@Valid Item item,BindingResult bresult,
			HttpServletRequest request) {
		//bresult : 유효성 검사의 결과
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) { //입력값 검증시 오류 발생
			return mav;  // item/create.jsp 페이지로 이동
		}
		// 입력값이 정상인 경우
		service.itemCreate(item,request); //db에 등록 + 이미지파일 업로드
		mav.setViewName("redirect:list");  //list 재 요청
		return mav;
	}
}
