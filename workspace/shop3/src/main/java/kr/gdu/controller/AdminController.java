package kr.gdu.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.gdu.domain.User;
import kr.gdu.dto.MailDto;
//import kr.gdu.dto.MailDto;
import kr.gdu.dto.UserDto;
import kr.gdu.exception.ShopException;
import kr.gdu.service.UserService;
import kr.gdu.util.CipherUtil;

@Controller
@RequestMapping("admin")
public class AdminController {
	@Autowired 
	private UserService service;
	
	private UserDto emailEncrypt(UserDto user) {
		String key = CipherUtil.makehash(user.getUserid()).substring(0,16);
		String plainEmail = CipherUtil.encrypt(user.getEmail(),key);
		user.setEmail(plainEmail);
		return user;
	}
	private User emailDecrypt(User user) {
		String key = CipherUtil.makehash(user.getUserid()).substring(0,16);
		String plainEmail = CipherUtil.decrypt(user.getEmail(),key);
		user.setEmail(plainEmail);
		return user;
	}

	@RequestMapping("list")
	public ModelAndView list(String sort,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		List<User> list = service.userlist();
		for (User u : list) {
			if (u.getEmail() != null)
			   u = emailDecrypt(u);
		}
		mav.addObject("list",list);
		return mav;
	}
	@GetMapping("mail")
	public ModelAndView mailform(String[] idchks, HttpSession session) {
		ModelAndView mav = new ModelAndView("admin/mail");
		if(idchks == null || idchks.length == 0) {
			throw new ShopException("메일을 보낼 대상자를 선택하세요","list");
		}
		//list : idchks파라미터의 속한 userid의 User 객체들
		List<User> list = service.getUserList(idchks);
		MailDto mail = new MailDto();
		StringBuilder recipient = new StringBuilder();
		for (User u : list) { 
			u = emailDecrypt(u); 
			recipient.append(u.getUsername())
			         .append("<").append(u.getEmail()).append(">,");
		}
		mail.setRecipient(recipient.toString());
		mav.addObject("mailDto",mail); //recipient 정보만 등록. 
		return mav;
	}
	@PostMapping("mail")
	public ModelAndView mail(@Valid MailDto mail,BindingResult bresult,
			HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			return mav;
		}
		mav.setViewName("alert");
		if(service.mailSend(mail)) {
			mav.addObject("message","메일 전송이 완료 되었습니다.");
		} else {
			mav.addObject("message","메일 전송을 실패 했습니다.");
		}
		mav.addObject("url","list");
		//첨부 파일 제거하기
		service.mailfileDelete(mail);
		return mav;
	}	
}





