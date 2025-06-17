package kr.gdu.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
	// MultipartFile : 업로드된 이미지 파일
	public String summernoteImageUpload(@RequestParam("image") MultipartFile multipartFile) {
		String url = service.summernoteImageUpload(multipartFile);
		return url;
	}
	
	// 시,군,동
	@RequestMapping(value="select1", produces="text/plain; charset=utf-8")
	public String sidoSelect1(String si, String gu) {
		return service.sidoSelect1(si,gu);
	}
	@RequestMapping("select2")
	public List<String> sigunSelect2(String si, String gu){
		return service.sigunSelect2(si,gu); // 리스트 객체를 클라이언트로 직접 전달
		// 클라이언트에서 오류발생 가능
		// pom.xml에 fasterxml.jackson... 설정이 필요함
		// 현재는 오류발생 안함 : 자동으로 변형해줌
	}
	
	// 메인로고
	@RequestMapping(value="mainLogo", produces="text/html; charset=utf-8")
	public String mainLogo(){
		return service.mainLogo();
	}
	
	// 수출입은행 환율정보 조회하기
	@RequestMapping(value="exchange1", produces="text/html; charset=utf-8")
	public String exchange1(){
		return service.exchange1(); // 미국, 중국, 일본, 유로 4개의 통화만 처리
	}
	// 수출입은행 환율정보 조회하기
	@RequestMapping("exchange2")
	public Map<String, Object> exchange2(){ // json 데이터로 전송
		return service.exchange2(); // 미국, 중국, 일본, 유로 4개의 통화만 처리
	}
	
	// 그래프
	@RequestMapping("graph1")
	public List<Map.Entry<String,Integer>> graph1(String id){ 
		Map<String, Integer> map = service.graph1(id);
		List<Map.Entry<String, Integer>> list = new ArrayList<>();
		for(Map.Entry<String, Integer> m : map.entrySet()) {
			list.add(m);
		}
		Collections.sort(list, (m1,m2) -> m2.getValue() - m1.getValue());
		return list;
	}
}
