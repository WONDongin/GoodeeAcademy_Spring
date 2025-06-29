package kr.gdu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("chat")
public class ChatController {
	@RequestMapping("*") //요청되는 화면 출력
	public String chat() {
		return null; //요청 url과 같은 뷰를 선택
		    //http://localhost:8080/chat/chat
            //  => 뷰 /WEB-INF/views/chat/chat.jap
	}
}
