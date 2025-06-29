package kr.gdu.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.gdu.domain.Sale;
import kr.gdu.domain.User;
import kr.gdu.dto.UserDto;
import kr.gdu.exception.ShopException;
import kr.gdu.service.ShopService;
import kr.gdu.service.UserService;
import kr.gdu.util.CipherUtil;
import kr.gdu.util.ShopUtil;

@Controller
@RequestMapping("user")
public class UserController {
	@Autowired
	private UserService service;
	@Autowired
	private ShopService shopService;
	
	@GetMapping("*")  //Get 방식 모든 요청시 호출
	public ModelAndView form() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new UserDto());
		return mav; //url과 연동된 뷰를 호출
	}
	@PostMapping("join")
	//BindingResult은 입력값 검증대상 변수의 다음에 와야함
	public ModelAndView userAdd(@Valid UserDto user,BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		if (bresult.hasErrors()) {
			//추가 오류 메세지 등록. global error로 추가하기
			bresult.reject("error.input.user");
			bresult.reject("error.input.check");
			return mav;
		}
		//정상적으로 입력된 경우
		try {
		  //1. 비밀번호 해쉬함수로 해쉬값으로 변경
		  //2. 이메일을 암호화 처리
		  //   userid의 해쉬값
		  //   email 암호화	
		  String cipherPass = CipherUtil.makehash(user.getPassword());
		  user.setPassword(cipherPass);
		  String cipherUserid = CipherUtil.makehash(user.getUserid());
		  String cipherEmail = CipherUtil.encrypt
				                  (user.getEmail(), cipherUserid);
		  user.setEmail(cipherEmail);
		  service.userInsert(
				  kr.gdu.domain.User.builder()
				   .userid(user.getUserid())
			       .password(cipherPass)
			       .username(user.getUsername())
			       .email(cipherEmail)
			       .address(user.getAddress())
			       .phoneno(user.getPhoneno())
			       .birthday(user.getBirthday()).build());
		} catch (DataIntegrityViolationException e) {//키값 중복된 경우
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
	 * 1. /WEB-INF/views/user/login.jsp 페이지 출력
	 * 2. 네이버 로그인 link 생성
	 */
	@GetMapping("login") 
	public ModelAndView loginForm(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		String clientId="네이버의 client Id";
		clientId = "CfrRBFs5rfhJxTkwkm4V";
		String redirectURL = null;
		try {
			//콜백URL 설정 => 네이버에서 정상처리로 결정되면 호출해주는 URL
			redirectURL = URLEncoder.encode
					   ("http://localhost:8083/user/naverlogin","UTF-8");
		} catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		SecureRandom random = new SecureRandom();
		//130자리 임의의 큰정수값
		String state = new BigInteger(130,random).toString();
		
		String apiURL = 
		 "https://nid.naver.com/oauth2.0/authorize?response_type=code";
		apiURL += "&client_id="+clientId;
		apiURL += "&redirect_uri="+redirectURL;
		apiURL += "&state="+state;
		mav.addObject(new UserDto());
		mav.addObject("apiURL",apiURL);
		session.getServletContext().setAttribute("session",session);
		return mav;
	}
	
	/*
		1. userid 맞는 User를 db에서 조회하기
		2. 비밀번호 검증  
		   일치 : session.setAttribute("loginUser",dbUser) => 로그인 정보
		   불일치 : 비밀번호를 확인하세요. 출력 (error.login.password)
		3. 비밀번호 일치하는 경우 mypage로 페이지 이동 => 404 오류 발생 (임시)
	 */
	@PostMapping("login") //db의 내용으로 기본 로그인 방식
	public ModelAndView login(UserDto user,BindingResult bresult,
			HttpSession session) {
		// session : 세션 객체 제공
		if(user.getUserid().trim().length() < 3 || 
		   user.getUserid().trim().length() > 10) {
			//@Valid 어노테이션에서 등록 방식으로 처리
			//messages.properties 파일에 error.required.userid로 메세지 등록
			bresult.rejectValue("userid", "error.required");
		}
		if(user.getPassword().trim().length() < 3 || 
		   user.getPassword().trim().length() > 10) {
			bresult.rejectValue("password", "error.required");
		}
		ModelAndView mav = new ModelAndView("user/login");
		if(bresult.hasErrors()) { //등록된 오류 존재?
			//global error 등록
			bresult.reject("error.input.check");
			return mav;
		}
		User dbUser = service.selectUser(user.getUserid());
		if(dbUser == null) { //아이디 없음
			bresult.reject("error.login.id");
			return mav;
		}
		//CipherUtil.makehash(user.getPassword())
		// 입력받은 비밀번호를 해쉬값으로 변경하여, db의 비밀번호와 비교
		if(CipherUtil.makehash(user.getPassword()).equals
				                    ( dbUser.getPassword())) { //비밀번호일치
		   session.setAttribute("loginUser", dbUser);
		   mav.setViewName("redirect:mypage?userid=" + user.getUserid());
		} else {
			bresult.reject("error.login.password");
			return mav;
		}
	return mav;
	}
	@RequestMapping("naverlogin")
	public String naverlogin(String code, String state, HttpSession session) {
		String clientId = "클라이언트 아이디값";//애플리케이션 클라이언트 아이디값";
		clientId = "CfrRBFs5rfhJxTkwkm4V";
		String clientSecret = "클라이언트 시크릿값";//애플리케이션 클라이언트 시크릿값";
		clientSecret = "h_lNha25GY";		
 	    String redirectURI=null;
		try {
			redirectURI = URLEncoder.encode("YOUR_CALLBACK_URL", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		 String apiURL;
		 apiURL = 
     "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
		 apiURL += "client_id=" + clientId;
		 apiURL += "&client_secret=" + clientSecret;
		 apiURL += "&redirect_uri=" + redirectURI;
		 apiURL += "&code=" + code;  //네이버에서 전달해준 파라미터값
		 apiURL += "&state=" + state; //네이버에서 전달해준 파라미터값. 초기에는 로그인 시작시 개발자가 전달한 임의의 수
		 System.out.println("code="+code+",state="+state);
//		 String access_token = "";
//		 String refresh_token = "";
		 StringBuffer res = new StringBuffer();
		 System.out.println("apiURL="+apiURL);
		 try {
		      URL url = new URL(apiURL);
		      HttpURLConnection con = //apiURL 연결 성공
		    		  (HttpURLConnection)url.openConnection();
		      con.setRequestMethod("GET");
		      //네이버에서 전달한 응답 코드
		      int responseCode = con.getResponseCode();
		      BufferedReader br; //네이버의 응답 메세지 저장
		      if(responseCode==200) { // 정상 호출
		        br = new BufferedReader
		        		(new InputStreamReader(con.getInputStream()));
		      } else {  // 에러 발생
		        br = new BufferedReader
		        		(new InputStreamReader(con.getErrorStream()));
		      }
		      String inputLine;
		      while ((inputLine = br.readLine()) != null) {
		        res.append(inputLine);
		      }
		      br.close();
		 } catch (Exception e) {
		      System.out.println(e);
		 }
		 // 네이버의 응답 형식 : JSON 형식
		 // JSON 형태의 문자열 데이터 => json 객체로 변경하기 위한 객체 생성
		 JSONParser parser = new JSONParser(); //pom.xml에 추가
		 JSONObject json=null;
		 try {
			json = (JSONObject)parser.parse(res.toString());
		 } catch (ParseException e) {
			e.printStackTrace();
		 }
		 String token = (String)json.get("access_token");//네이버가 전달해준 토큰
		 String header = "Bearer " + token; //한개의 공백 필요
		 try {
		    apiURL = "https://openapi.naver.com/v1/nid/me";
		    URL url = new URL(apiURL);
		    HttpURLConnection con = (HttpURLConnection)url.openConnection();
		    con.setRequestMethod("GET");
		    con.setRequestProperty("Authorization", header);
		    int responseCode = con.getResponseCode();
		    BufferedReader br;
		    res = new StringBuffer();
		    if(responseCode==200) {
		        br = new BufferedReader
		        		(new InputStreamReader(con.getInputStream()));
		    } else { 
		        br = new BufferedReader
		        		(new InputStreamReader(con.getErrorStream()));
		    }
		    String inputLine;
		    while ((inputLine = br.readLine()) != null) {
		        res.append(inputLine);
		    }
		    br.close();
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 //res : 로그인한 사용자의 정보 전달. JSON 형식으로 전달
		 System.out.println("네이버 최종 응답 res="+res);
		 try {
			json = (JSONObject)parser.parse(res.toString());
		} catch (ParseException e) {
			e.printStackTrace();
			throw new ShopException("네이버 로그인시 오류 발생","login");
		}
		JSONObject jsondetail = (JSONObject)json.get("response");
		String userid = jsondetail.get("id").toString(); //네이버에서 전달해준 ID값
		User user = service.selectUser(userid); //
		if (user == null) { //처음 로그인한 경우
			user = new User();
			user.setUserid(userid);
			user.setUsername(jsondetail.get("name").toString());
			String email = jsondetail.get("email").toString();
			user.setEmail(email);
			user.setPhoneno(jsondetail.get("mobile").toString());
			user.setChannel("naver");//현재는 db의 컬럼에서 빠짐
			service.userInsert(user);
		}
		session.setAttribute("loginUser", user);
		return "redirect:mypage?userid="+user.getUserid();
	}
	
	/*
	 * AOP 설정필요 : UserLoginAspect 클래스의 userIdCheck 메서드로 구현
	 *  1. 로그여부 검증
	 *     로그아웃상태인 경우 로그후 거래메세지 출력. login 페이지로 이동
	 *  2. 본인 거래 여부 검증
	 *     admin이 아니면서 다른 사용자 정보 출력 불가   
	 */
	//2025-06-25 : 이메일을 복호화 하여 mypage 화면에 출력하기
	@RequestMapping("mypage")
	public ModelAndView idCheckMypage(String userid,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.selectUser(userid);
		user = emailDecrypt(user);
		//Sale : db정보,  주문상품정보
		List<Sale> salelist = shopService.saleList(userid);
		mav.addObject("user", user);
		mav.addObject("salelist", salelist);
		return mav;
	}	
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:login";
	}
	//로그인 상태, 본인정보만 조회 검증 => AOP 클래스(UserLoginAspect.userIdCheck() 검증)
	@GetMapping({"update","delete"})
	public ModelAndView idCheckUser(String userid,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.selectUser(userid);
		user = emailDecrypt(user);
		mav.addObject("userDto",new UserDto(user));
		return mav;
	}
	@PostMapping("update")
	public ModelAndView idCheckUpdate(@Valid UserDto userDto, 
			BindingResult bresult,String userid, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			bresult.reject("error.update.user"); //global error
			return mav;
		}
		//비밀번호 검증
		String encryptPass = CipherUtil.makehash(userDto.getPassword());
		User loginUser = (User)session.getAttribute("loginUser");
		if(!loginUser.getPassword().equals(encryptPass)) {
			bresult.reject("error.login.password");
			return mav;
		}
		//비밀번호 일치인 경우 실행
		try {
			User user = new User(userDto);
			user = emailEncrypt(user);
			user.setPassword(encryptPass);
			service.userUpdate(user);
			if(loginUser.getUserid().equals(user.getUserid())) {
				//로그인 정보의 데이터를 수정된 데이터로 변경
				session.setAttribute("loginUser", user);
			}
			mav.setViewName("redirect:mypage?userid="+user.getUserid());
		} catch(Exception e) {
			e.printStackTrace();
			throw new ShopException("고객 정보 수정 실패",
					"update?userid=" + userDto.getUserid());
		}
		return mav;
	}
	//	/*
//	 * UserLoginAspect.userIdCheck() 메서드 실행 설정
//	 * 탈퇴 검증
//	 * 1. 관리자인 경우 탈퇴 불가
//	 * 2. 비밀번호 검증 => 로그인된 비밀번호와 비교
//	 *     본인탈퇴시 : 본인 비밀번호로 검증
//	 *     관리자타인탈퇴 : 관리자 비밀번호로 검증
//	 * 3. 비밀번호 불일치
//	 *    메세지 출력 후 delete 페이지로 이동
//	 * 4. 비밀번호 일치
//	 *    db에서 사용자정보 삭제
//	 *    본인탈퇴 : 로그아웃. login페이지로 이동
//	 *    관리자 타인 탈퇴 : admin/list 페이지 이동    
//	 */
	@PostMapping("delete")
	public ModelAndView idCheckDelete(String password,String userid,
			  HttpSession session) {
		ModelAndView mav = new ModelAndView();
		// 관리자 탈퇴 불가
		if(userid.equals("admin"))
			throw new ShopException("관리자 탈퇴는 불가합니다.", 
					                "mypage?userid="+userid);
		//비밀번호 검증 : 로그인된 정보
		User loginUser = (User)session.getAttribute("loginUser");
		//비밀번호 불일치
		if(!CipherUtil.makehash(password).equals(loginUser.getPassword())) {
			throw new ShopException("비밀번호를 확인하세요.", 
					                "delete?userid="+userid);
		}
		//비밀번호 일치 : 고객정보 제거
		try {
			service.userDelete(userid);
		} catch(Exception e) {
			e.printStackTrace();
			throw new ShopException("탈퇴시 오류발생.", "delete?userid="+userid);
		}
		//탈퇴 성공
		String url = null;
		if(loginUser.getUserid().equals("admin")) {  //관리자가 강제 탈퇴
			url = "redirect:../admin/list";
		} else {           //본인 탈퇴. 
			session.invalidate();
			url = "redirect:login";
		}	
		mav.setViewName(url);
		return mav;
	}
	/*
	 * 본인의 비밀번호만 변경 가능. 
	 * 1. 로그인 검증 => AOP 클래스
	 *    UserLoginAspect.loginCheck() 
	 *     pointcut : UserController.loginCheck*(..)인 메서드이고,
	 *                마지막 변수가 HttpSession인 메서드
	 *     advice : around
	 * 2. 현재db의 비밀번호와 파라미터 현재비밀번호 일치 검증
	 * 3. 비밀번호 일치 : db 수정. 로그인정보 변경. mypage 페이지로 이동
	 * 4. 비밀번호 불일치 : 오류 메세지 출력. password 페이지로 이동                
	 */
	@ GetMapping("password")
	public String loginCheckform (HttpSession session) {
		return null;
	}
	@PostMapping("password")
	public String loginCheckPassword
	   (String password,String chgpass,HttpSession session) {
		User loginUser = (User)session.getAttribute("loginUser");
		//password : 입력한 현재 비밀번호
		//loginUser.getPassword() : 로그인 정보. db에 저장된 정보
		if(!CipherUtil.makehash(password).equals(loginUser.getPassword())) {
		  throw new ShopException("비밀번호 오류 입니다.","password");
		}
		//비밀번호 일치
		try {		
			//db의 비밀번호 변경
			String hashPassword = CipherUtil.makehash(chgpass);
			service.userChgpass(loginUser.getUserid(),hashPassword);
			loginUser.setPassword(hashPassword); //로그인 정보에 비밀번호 수정
		} catch(Exception e) {
			e.printStackTrace();
			throw new ShopException
			  ("비밀번호 수정시 db 오류 입니다.","password");
		}
		return "redirect:mypage?userid="+loginUser.getUserid();
	}
	@PostMapping("{url}search") //idsearch 요청. url=id
	public ModelAndView search(UserDto userDto, BindingResult bresult,
			@PathVariable String url) {
		//@PathVariable :{url}의 값을 매개변수로 전달.
		//idsearch 요청 : url = id
		//pwsearch 요청 : url = pw
		ModelAndView mav = new ModelAndView();
		String code = "error.userid.search";
		String title = "아이디";
		if(url.equals("pw")) {
			title = "비밀번호 초기화";
			code = "error.password.search";
			if(userDto.getUserid() == null || 
					userDto.getUserid().trim().equals("")) {
				bresult.rejectValue("userid", "error.required"); 
			}
		}
		if(userDto.getEmail() == null || userDto.getEmail().trim().equals("")) {
			bresult.rejectValue("email", "error.required"); 
		}
		if(userDto.getPhoneno() == null || userDto.getPhoneno().trim().equals("")) {
			bresult.rejectValue("phoneno", "error.required");
		}
		if(bresult.hasErrors()) {
			bresult.reject("error.input.check");
			return mav;
		}
		//입력값 정상인 경우
		if(userDto.getUserid() != null && userDto.getUserid().trim().equals(""))
			userDto.setUserid(null); //빈문자열("")인 경우 null 변경
		User user = new User(userDto);
		String result =service.getSearch(user);
		if(result == null) { //검색된 아이디나 비밀번호가 없는 경우
			bresult.reject(code);
		    return mav;
  	    }
		//아이디 또는 비밀번호를 검색한 경우
		if(url.equals("pw")) {
			//result : 영문자,숫자 중 임의의 6개의 문자를 저장
			result =  ShopUtil.getRandomString(6,true,true);
			String hashresult = CipherUtil.makehash(result); //해쉬값으로 변경
			//초기화된 비밀번호로 DB의 비밀번호 변경
			service.userChgpass(user.getUserid(), hashresult); 
		}
		mav.addObject("result",result);
		mav.addObject("title",title);
		mav.setViewName("search");
		return mav;
	}
	private User emailEncrypt(User user) {
		String key = CipherUtil.makehash(user.getUserid()).substring(0,16);
		String plainEmail = CipherUtil.encrypt(user.getEmail(),key);
		user.setEmail(plainEmail);
		return user;
	}
	//암호화된 이메일을 가진 user 객체에서 이메일 부분을 복호화 하여 user 객체 리턴
	private User emailDecrypt(User user) {
		String key = CipherUtil.makehash(user.getUserid()).substring(0,16);
		String plainEmail = CipherUtil.decrypt(user.getEmail(),key);
		user.setEmail(plainEmail);
		return user;
	}
	
}