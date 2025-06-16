package kr.gdu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.gdu.service.BoardService;
/*
@Controller : @Component + Controller 기능
	Mapping 메서드의 리턴 타입 : ModelAndView : 뷰이름 + 데이터
	Mapping 메서드의 리턴 타입 : String : 뷰이름 
	
@RestController : @Component + Controller 기능 + 클라이언트로 데이터를 직접 전송
	Mapping 메서드의 리턴 타입 : String : 클라이언트로 전달되는 문자열 값
	Mapping 메서드의 리턴 타입 : Object : 클라이언트로 전달되는 값. json 형식처리

Spring 4.0 이후에 추가됨
Spring 4.0 이전에는 @ResponseBody 기능으로 사용함
@ResponseBody의 설정은 메서드 마다 설정함
*/
@RestController 
@RequestMapping("ajax")
public class AjaxController {
	@Autowired
	BoardService service;
	// 이미지 업로드
	// produces="text/plain; charset=utf-8" : 전송될 데이터 형식
	@PostMapping(value="uploadImage", produces="text/plain; charset=utf-8")
	public String summernoteImageUpload(@RequestParam("image") MultipartFile multipartFile) {
		return service.summernoteImageUpload(multipartFile);
	}
}
