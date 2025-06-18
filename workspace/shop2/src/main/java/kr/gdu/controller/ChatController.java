package kr.gdu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("chat")
public class ChatController {
	@RequestMapping("*")
	public String chat() {
		return null;
	}
}
