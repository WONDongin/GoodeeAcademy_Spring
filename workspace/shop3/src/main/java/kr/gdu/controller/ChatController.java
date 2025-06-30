package kr.gdu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("chat")
public class ChatController {
	@RequestMapping("*") //요청되는 화면 출력
	public String dumy() {
		return null; //요청 url과 같은 뷰를 선택
		    //http://localhost:8080/chat/chat
            //  => 뷰 /WEB-INF/views/chat/chat.jap
	}
	@RequestMapping("chat")
	public String chat(Model model, HttpServletRequest request) {
		model.addAttribute("port",request.getLocalPort());
		model.addAttribute("server",request.getServerName());
		model.addAttribute("path",request.getContextPath());
		return "chat/chat";
	}
}
