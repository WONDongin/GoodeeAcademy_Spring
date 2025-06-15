package kr.gdu.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.gdu.logic.User;
import kr.gdu.service.UserService;

@Controller
@RequestMapping("admin")
public class AdminController {
	@Autowired 
	private UserService service;

	@RequestMapping("list")
	public ModelAndView list(String sort,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		List<User> list = service.userlist();
		mav.addObject("list",list);
		return mav;
	}
}





