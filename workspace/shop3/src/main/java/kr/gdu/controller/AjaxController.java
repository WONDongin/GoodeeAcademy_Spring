package kr.gdu.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import kr.gdu.service.BoardService;
import kr.gdu.service.ChatBotService;
/*
 * @Controller : @Component + Controller 기능
 *     Mapping 메서드의 리턴 타입 : ModelAndView : 뷰이름 + 데이터
 *     Mapping 메서드의 리턴 타입 : String : 뷰이름
 * @RestController : @Component + Controller 기능 + 클라이언트로 데이터를 직접 전송
 *     Mapping 메서드의 리턴 타입 : String : 클라이언트로 전달되는 문자열 값     
 *     Mapping 메서드의 리턴 타입 : Object : 클라이언트로 전달되는 값. JSON 형식처리
 *     
 * Spring 4.0 이후에 추가됨.
 * Spring 4.0 이전에는 @ResponseBody 기능으로 사용함
 * @ResponseBody의 설정은 메서드마다 설정함.       
 */
@RestController
@RequestMapping("ajax")
public class AjaxController {
	@Autowired
	BoardService service;
	@Autowired
	ChatBotService chatService;

	//produces="text/plain; charset=utf-8" : 전송될 데이터 형식
	@PostMapping(value="uploadImage",produces="text/plain; charset=utf-8")
	  public String summernoteImageUpload
	   (@RequestParam("image") MultipartFile multipartFile) {
		//multipartFile : 업로드된 이미지 파일
		String url = service.summernoteImageUpload(multipartFile);

		return url; // /board/image/파일명
	  }
	@RequestMapping(value="select1",produces="text/plain; charset=utf-8")
	  public String sidoSelect1(String si, String gu) {
		return service.sidoSelect1(si,gu);
	  }
	@RequestMapping("select2")
	public List<String> sigunSelect2(String si, String gu) {
		return service.sigunSelect2(si,gu); //리스트 객체를 클라이언트로 직접 전달
		//클라이언트 에서 오류발생 가능
		// pom.xml에 fasterxml.jackson.... 설정이 필요함.
		// 현재는 오류 발생 안함 : 자동으로 변형 해줌
	}
	@RequestMapping(value="exchange1", produces="text/html; charset=utf-8")
	public String exchange1() {
		return service.exchange1(); //미국달러,중국,일본,유로 4개의 통화만 처리
	}
	@RequestMapping("exchange2")
	public Map<String,Object> exchange2() {  //json 데이터로 전송
		return service.exchange2(); //미국달러,중국,일본,유로 4개의 통화만 처리
	}
	@RequestMapping("graph1")
	public List<Map.Entry<String, Integer>> graph1(String id) {
		Map<String,Integer> map = service.graph1(id); //글쓴이별 등록 건수
		//map : {홍길동:10,김삿갓:7,...}
		List<Map.Entry<String, Integer>> list = new ArrayList<>();
		for(Map.Entry<String, Integer> m : map.entrySet() ) {
			list.add(m); // [{홍길동:10},{김삿갓:7},...]
		}
		//cnt값의 큰순으로 정렬하기
		Collections.sort(list,(m1,m2)->m2.getValue() - m1.getValue());
		return list; // [{홍길동:10},{김삿갓:7},...]
	}	
	@RequestMapping("goodeelogo")
	public String goodeelogo() {
		String src = null;
		Document doc = null;
		String url = "http://gudi.kr/";
		Elements images = null;
		try {
			doc = Jsoup.connect(url).get();
			images = doc.select("#logo_w2024071714eb07c47011e > div > a > img.scroll_logo.fixed_transform"); //logo
			for(Element img : images) {
				src = img.attr("src"); //gudi.kr 페이지의 이미지의 src 속성값
				System.out.println(src);
				break;
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return src;	               
	}
	@RequestMapping("graph2")
	public List<Map.Entry<String, Integer>> graph2(String id) {
	   Map<String,Integer> map = service.graph2(id);
	   // map : {2025-06-05=5,2025-06-04=7,....}
	   List<Map.Entry<String, Integer>> list =  
			                 new ArrayList<>(map.entrySet());
	   return list;	 //[{2025-06-05=5},{2025-06-04=7},....]              
	}
	@PostMapping("gptquestion")
	public String qptquestion (String question) {
      String response = null;
	  try {
		response = chatService.getChatGPTResponse(question);
	  } catch (URISyntaxException | IOException | InterruptedException e) {
		e.printStackTrace();
	  }
//	  System.out.println(response);
	  return response;
	}	
}