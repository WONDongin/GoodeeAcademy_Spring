package kr.gdu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import kr.gdu.dto.MemberDto;
import kr.gdu.entity.MemberEntity;
import kr.gdu.service.MemberService;

@RestController
@RequestMapping("/member/")
@CrossOrigin(origins="http://localhost:3000", allowCredentials="true")
public class MemberController {

	@Autowired
	MemberService service; 
	
	@PostMapping("joinPro")
	public void joinPro(MemberDto dto) {
		service.memberInsert(new MemberEntity(dto));
	}
	@PostMapping("loginPro")
	public ResponseEntity loginPro(String id, String pass, String key, 
			HttpServletResponse response,
			@CookieValue(value="id",required=false) Cookie cookie) {
		if (cookie == null) {
			cookie = new Cookie("id","");
			response.addCookie(cookie); //응답 객체에 생성된 쿠키를 저장
		}
		MemberEntity mem = service.getMember(id);
		if(mem != null) {
			if(mem.getPass().equals(pass)) { //비밀번호
				cookie.setValue(id);
				cookie.setDomain("localhost"); //서버의 IP 주소
				cookie.setPath("/"); 
				cookie.setMaxAge(30); //쿠키 허용 시간 30초
				// http:// => 일반 접속. port : 80
				// https:// => SSL 보안 서버. port : 443. 암호화
				cookie.setSecure(true); //보안쿠키. localhost에서는 의미 없음. SSL 접속시만 쿠키 전송
				response.addCookie(cookie); //쿠키저장. 브라우저에 저장
				return new ResponseEntity("{\"message\":\"login_success\",\"token\":\"1\"}",
						HttpStatus.OK); 
			} else {
				return new ResponseEntity("{\"message\":\"비밀번호 오류\",\"token\":\"0\"}",
						HttpStatus.UNAUTHORIZED); 
			}
		} else {
			return new ResponseEntity("{\"message\":\"아이디 없음\",\"token\":\"0\"}",
					HttpStatus.UNAUTHORIZED); 
		}
	}
	
}
