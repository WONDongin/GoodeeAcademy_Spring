package kr.gdu.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.gdu.domain.Item;
import kr.gdu.dto.ItemDto;
import kr.gdu.service.ShopService;

@Controller   //@Component + Controller 기능. 객체화됨
@RequestMapping("item")  
public class ItemController {
	@Autowired 
	private ShopService service;
	@RequestMapping("list") 
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		List<Item> itemList = service.itemList();
		mav.addObject("itemList",itemList);
		return mav;
	}
	@GetMapping({"detail","update","delete"}) //Get 방식 요청시 호출
	public ModelAndView detail(Integer id) { 
		ModelAndView mav = new ModelAndView();
		Item item = service.getItem(id);
		mav.addObject("item",item);
		return mav;
	}
	@GetMapping("create")  //Get 방식 요청
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new ItemDto());
		return mav;
	}
	@PostMapping("create") //Post 방식 요청
	public ModelAndView register(@Valid ItemDto item,BindingResult bresult,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) { //입력값 검증시 오류 발생
			return mav;  // item/create.jsp 페이지로 이동
		}
		//입력값이 정상인 경우
		service.itemCreate(item,request); //db에 등록 + 이미지파일 업로드
		mav.setViewName("redirect:list");  //list 재 요청
		return mav;
	}
	@PostMapping("update")
	public ModelAndView update(@Valid ItemDto item, BindingResult bresult, 
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			return mav;
		}
		/*
		 * item : id,name,price,description,pictureUrl 입력값 저장
		 */
		service.itemUpdate(item,request);
		mav.setViewName("redirect:list");
		return mav;
	}
	@PostMapping("delete")
	public String delete(Integer id) {
		service.itemDelete(id);
		return "redirect:list"; //뷰선택
	}
	
}
