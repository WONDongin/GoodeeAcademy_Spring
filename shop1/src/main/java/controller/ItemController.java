package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import service.ShopService;

@Controller //@Component 하위에있는 + Controller 기능. 객체화
@RequestMapping("item") // http://localhost:8080/shop1/item
public class ItemController {
	@Autowired // ShopService 객체주입
	private ShopService service;
	
	// (상품 리스트) http://localhost:8080/shop1/item/list. 요청시 호출되는 메서드(핸들러 매핑에서 처리)
	@RequestMapping("list")                        // Get, Post 방식 요청시 호출
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();     // ModelAndView : View에 이름과, 전달 데이터를 저장
		List<Item> itemList = service.itemList();  // itemList : item 테이블의 모든 정보를 저장 객체
		mav.addObject("itemList", itemList);       // view에 전달할 객체 저장
		// mav.setViewName("item/list"); view 이름이 null인 경우 요청 URL에서 가져옴
		return mav;
	}
	
	// (상품 상세 보기) http://localhost:8080/shop1/item/detail?id=1 요청
	// (상품 상세 보기) http://localhost:8080/shop1/item/update?id=1 요청
	// (상품 상세 보기) http://localhost:8080/shop1/item/delete?id=1 요청
	@GetMapping({"detail", "update", "delete"})    // Get 방식 요청시 호출
	public ModelAndView detail(Integer id) {       // Integer id : id 파라미터값을 저장. 매개변수명과 파라미터 이름이 같아야한다.
		ModelAndView mav = new ModelAndView();
		Item item = service.getItem(id);
		mav.addObject("item",item);
		return mav;
	}
	
	// (상품등록)
	@GetMapping("create") // Get 방식 요청
	public ModelAndView create() {  
		ModelAndView mav = new ModelAndView();
		mav.addObject(new Item());
		return mav;
	}
	@PostMapping("create") // Post 방식 요청
	/*
	Item item : 파라미터 이름과 item 클래스의 setProperty를 이용하여 파라미터값 저장
	Item item = new Item();
	item.setName(request.getParameter("name"));
	
	@Valid : Item 클래스에 정의된 내용으로 유효성 검증
	bresult : 유효성 검증의 결과 저장됨
	request : 파일 업로드 위치 선정에 사용
	*/
	public ModelAndView register(@Valid Item item, BindingResult bresult, HttpServletRequest request) {  
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) { // 입력값 검증시 오류 발생
			return mav;           // item/create.jsp 페이지로 이동
		}
		
		// 입력값 정상인 경우
		service.itemCreate(item,request); // 상풍 DB등록 + 사진 업로드
		mav.setViewName("redirect:list"); // list 재 요청(list 페이지 이동)
		return mav;
	}
	
	// (상품수정)
	@PostMapping("update") 
	public ModelAndView update(@Valid Item item, BindingResult bresult, HttpServletRequest request) {  
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) { // 입력값 검증시 오류 발생
			return mav;           // item/update.jsp 페이지로 이동
		}
		/*
		item : id,name,price,description,pictureUrl 입력값
		*/
		service.itemUpdate(item, request);
		mav.setViewName("redirect:list");
		return mav;
	}
	
	// (상품삭제)
	@PostMapping("delete")
	public String delete(Integer id) {
		service.itemDelete(id);
		return "redirect:list";
	}
	
	
	
	
}
