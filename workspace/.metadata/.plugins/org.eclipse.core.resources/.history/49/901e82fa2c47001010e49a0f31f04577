package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import exception.ShopException;
import logic.User;
import service.UserService;

@Controller
@RequestMapping("user")
public class UserController {
	@Autowired
	private UserService service;
	
	@GetMapping("*") // Get 방식 모든 요청호출
	public ModelAndView form() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new User());
		return mav; // url과 연동된 뷰를 호출
	}
	
	@PostMapping("join")
	//BindingResult은 입력값 검증대상 변수의 다음에 와야함
	public ModelAndView userAdd(@Valid User user, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			// 추가 오류 메세지 등록
			bresult.reject("error.input.user");
			bresult.reject("error.input.check");
			return mav;
		}
		
		// 정상적으로 입력된 경우
		try {
			service.userInsert(user);
		}catch (DataIntegrityViolationException e) { // (아이디 중복)
			e.printStackTrace();
			bresult.reject("error.duplicate.user");
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			return mav;
		}
		mav.setViewName("redirect:login");
		return mav;
	}
	
	/*
	1. userid 맞는 User를 db에서 조회하기
	2. 비밀번호 검증
	일치 : session.setAttribute("loginUser", dbUser) => 로그인 정보
	불일치 : 비밀번호를 확인하세요. 출력 (error.login.password)
	3. mypage로 페이지 이동 => 404 오류 발생 (임시)
	*/
	@PostMapping("login")
	public ModelAndView login(User user, BindingResult bresult, HttpSession session) {
		if(user.getUserid().trim().length() < 3 ||user.getUserid().trim().length() > 10) {
			// @Valid 어노테이션에서 등록방식으로 처리
			// messages.properties 파일에 : error.required.userid로 메세지 등록
			bresult.rejectValue("userid", "error.required");
		}
		if(user.getPassword().trim().length() < 3 ||user.getPassword().trim().length() > 10) {
			bresult.rejectValue("password", "error.required");
		}
		
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) { // 등록된 오류 존재?
			// global error 등록
			bresult.reject("error.input.check");
			return mav;
		}
		
		User dbUser = service.selectUser(user.getUserid());
		if(dbUser == null) { // 아이디 없음
			bresult.reject("error.login.id");
			return mav;
		}
		if(user.getPassword().equals(dbUser.getPassword())) { // 비밀번호 일치
			session.setAttribute("loginUser", dbUser);
			mav.setViewName("redirect:mypage?userid=" + user.getUserid());
		}else { // 비밀번호 오류시
			bresult.reject("error.login.password");
			return mav;
		}
		return mav;
	}
	/*
	AOP 설정필요 : UserLoginAspect 클래스의 userIdCheck 메서드로 구현
	1.	로그인여부 검증
		로그아웃상태인 경우 로그아웃 후 거래 메세지 출력. login 페이지로 이동
	2. 	본인 거래 여부 검증
		admin이 아니면서 다른 사용자 정보 출력 불가
	*/
	// (마이페이지)
	@RequestMapping("mypage")
	public ModelAndView idCheckmypage(String userid, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.selectUser(userid);
		mav.addObject("user", user);
		return mav;
	}
	
	// (로그아웃)
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:login";
	}
	
	// 로그인상태, 본인 정보만 조회검증 => AOP 클래스(UserLoginAspect.userIdCheck()
	@GetMapping({"update","delete"})
	public ModelAndView idCheckUser(String userid,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.selectUser(userid);
		mav.addObject("user",user);
		return mav;
	}
	// (회원 정보수정)
	@PostMapping("update")
	public ModelAndView idCheckUpdate(@Valid User user, BindingResult bresult, String userid, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			bresult.reject("error.update.user"); // global error
			return mav;
		}
		// 비밀번호 검증
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.getPassword().equals(user.getPassword())) {
			bresult.reject("error.login.password");
			return mav;
		}
		// 비밀번호 일치인 경우 실행
		try {
			service.userUpdate(user);
			if(loginUser.getUserid().equals(user.getUserid())) {
				// 로그인 정보의 데이터를 수정된 테이터로 변경
				session.setAttribute("loginUser", user);
			}
			mav.setViewName("redirect:mypage?userid=" + user.getUserid());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ShopException("고객 정보 수정 실패", "update?userid=" + user.getUserid());
		}
		return mav;
	}
	
	/* (회원탈퇴)
	UserLoginAspect.userIdCheck() 메서드 실행 설정
	탈퇴검증
	1. 관리자인 경우 탈퇴불가
	2. 비밀번호 검증 => 로그인된 비밀번호와 비교
	   본인 탈퇴시 : 본인 비밀번호로 검증
	   관리자 탈퇴시 : 관리자 비밀번호로 검증
	3. 비밀번호 불일치 : 메시지 출력 후 delete 페이지로 이동
	4. 비밀번호 일치 : db에서 사용자 정보 삭제
	   본인 탈퇴시 : 로그아웃. login 페이지로 이동
	   관리자 타인 탈퇴 : admin/list 페이지로 이동
	   	
	*/
	@PostMapping("delete")
	public ModelAndView idCheckDelete(String password, String userid, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		// 관리자 탈퇴불가
		if(userid.equals("admin")) {
			throw new ShopException("관리자 탈퇴는 불가합니다", "mypage?userid=" + userid);
		}
		
		// 비밀번호 검증 : 로그인된 정보
		User loginUser = (User) session.getAttribute("loginUser");
		// 비밀번호 불일치
		if(!password.equals(loginUser.getPassword())) {
			throw new ShopException("비밀번호를 확인하세요", "delete?userid=" + userid);
		}
		// 비밀번호 일치 : 고객정보 제거
		try {
			service.userDelete(userid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ShopException("탈퇴시 오류발생", "delete?userid=" + userid);
		}
		// 탈퇴성공
		String url = null;
		if(loginUser.getUserid().equals("admin")) { // 관리자가 강제탈퇴
			url="redirect:../admin/list";
		} else { // 본인탈퇴
			session.invalidate();
			url="redirect:login";
		}
		mav.setViewName(url);
		return mav;
	}
	
	/* (비밀번호 변경)
	본인의 비밀번호만 변경 가능
	1. 로그인 검증 => AOP 클래스
	   UserLoginAspect.loginCheck()
	   pointcut : UserController.loginCheck*(..)인 메서드이고,
	   마지막 변수가 HttpSession인 메서드
	   
	   advice : around
	2. 현재 db의 비밀번호와 파라미터 현재 비밀번호 일치 검증
	3. 비밀번호 일치 : db 수정. 로그인정보 변경. mypage 페이지로 이동
	4. 비밀번호 불일치 : 오류 메시지 출력. password 페이지로 이동
	*/
	@GetMapping("password")
	public String loginCheckform(HttpSession session) {
		return null;
	}
	
	@PostMapping("password")
	public String loginCheckPassword(String password, String chgpass, HttpSession session) {
		// password : 입력한 현재 비밀번호
		// loginUser.getPassword() : 로그인 정보, db에 저장된 정보
		User loginUser = (User) session.getAttribute("loginUser");
		if(!password.equals(loginUser.getPassword())) {
			throw new ShopException("비밀번호 오류 입니다", "password");
		}
		// 비밀번호 일치
		try {
			// db의 비밀번호 변경
			service.userChgpass(loginUser.getUserid(), chgpass);
			loginUser.setPassword(chgpass); // 로그인 정보에 비밀번호 수정
		} catch (Exception e) {
			e.printStackTrace();
			throw new ShopException("비밀번호 수정시 db 오류 입니다.", "password");
		}
		return "redirect:mypage?userid=" + loginUser.getUserid();
	}
	
	//비밀번호초기화알고리즘
	public  String getTempPw() {
		List<String> lowerList = Arrays.asList
				("a" ,"b" ,"c" ,"d" ,"e" ,"f" ,"g" ,"h" ,"i" ,"j" ,"k"
						,"l" ,"m" ,"n" ,"o" ,"p","q","r","s","t","z");
		List<String> upperList = new ArrayList<>();
		for (String string : lowerList) {
			upperList.add(string.toUpperCase());
		}	
		List<Object> combineList = new ArrayList<>();
		combineList.addAll(lowerList);
		combineList.addAll(upperList);
		for (int i = 0; i < 30; i++) { //랜덤한0~9 숫자 30개집어넣기
			combineList.add(new Random().nextInt(10));
		}
		//무작위 섞기
		Collections.shuffle(combineList);
		String tempNum = "";
		for (int i = 0; i < 6; i++) {
			int num = new Random().nextInt(combineList.size());
			tempNum += combineList.get(num);
		}
		return tempNum;
	}

	// (id/pw 찾기)
	@PostMapping("{url}search") //idsearch 요청
	public ModelAndView search(User user, BindingResult bresult, @PathVariable String url) {
		// @PathVariable : {url}의 값을 매개변수로 전달
		// idsearch 요청 : url = id
		// pwsearch 요청 : url = pw
		ModelAndView mav = new ModelAndView();
		String code = "error.userid.search";
		String title = "아이디";
		if(url.equals("pw")) {
			title="비밀번호 초기화";
			code="error.password.search";
			if(user.getUserid() == null || user.getUserid().trim().equals("")) {
				bresult.rejectValue("userid", "error.required");
			}
		}
		if(user.getEmail() == null || user.getEmail().trim().equals("")) {
			bresult.rejectValue("email", "error.required");
		}
		if(user.getPhoneno() == null || user.getPhoneno().trim().equals("")) {
			bresult.rejectValue("phoneno", "error.required");
		}
		if(bresult.hasErrors()) {
			bresult.reject("error.input.check");
			return mav;
		}

		// 입력값 정상인 경우
		if(user.getUserid() != null && user.getUserid().trim().equals("")) user.setUserid(null);
		// 빈 문자열("")인 경우 null 변경
		// result : userId
		String result = service.getSearch(user);
		if(result == null) { // 검색된 아이디나 비밀번호가 없는 경우
			bresult.reject(code);
			return mav;
		}
		// 검색 완료인 경우
		mav.addObject("result",result);
		mav.addObject("title",title);
		mav.setViewName("search");
		return mav;
	}
	
	
}
