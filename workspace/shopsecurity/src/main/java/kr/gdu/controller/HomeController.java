package kr.gdu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	@GetMapping("/") //http://localhost:8080
	public String root(Model model) {
		model.addAttribute("msg","/로 요청");
		return "home";
	}
	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("msg","/home로 요청");
		return "home";
	}
	@GetMapping("/my")
	public String my(Model model) {
		model.addAttribute("msg","/my로 요청");
		return "home";
	}
	@GetMapping("/admin")
	public String admin(Model model) {
		model.addAttribute("msg","/admin로 요청");
		return "home";
	}
	@GetMapping("/login")
	public String login() {
		return "login";
	}
}
