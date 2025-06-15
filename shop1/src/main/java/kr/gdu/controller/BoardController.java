package kr.gdu.controller;

import java.util.List;
import java.util.Map;

import kr.gdu.service.ShopService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.gdu.exception.ShopException;
import kr.gdu.logic.Board;
import kr.gdu.service.BoardService;

@Controller
@RequestMapping("board")
public class BoardController {
	@Autowired
	private BoardService service;
	
	@GetMapping("*")
	public ModelAndView write() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new Board());
		return mav;
	}
	/* Spring에서 파라미터 전달 방식
	 *   1. 파라미터이름과 매개변수의 이름이 같은 경우 매핑
	 *   2. Bean 클래스의 프로퍼티명과 파라미터이름이 같은 경우 매핑
	 *   3. Map 객체에 RequestParam 어노테이션을 이용한 매핑
	 * 
	 * @RequestParam : 파라미터값을 Map 객체에 매핑하여 전달
	 */
	@RequestMapping("list")
	public ModelAndView list(@RequestParam Map<String,String> param,
			 HttpSession session) {
//		System.out.println("param:" + param);
		Integer pageNum = null;
		for(String key : param.keySet()) {
			if(param.get(key) == null || param.get(key).trim().equals("")) {
			   param.put(key, null);	
			}
		}
		if (param.get("pageNum") != null) {
			   pageNum = Integer.parseInt(param.get("pageNum"));
		} else { 
			pageNum = 1;
		}
		String boardid = param.get("boardid");
		if (boardid == null) boardid = "1";
		String searchtype = param.get("searchtype");
		String searchcontent = param.get("searchcontent");
		
		ModelAndView mav = new ModelAndView();
		String boardName = null;
		switch(boardid) {
		   case "1" : boardName = "공지사항"; break;
		   case "2" : boardName = "자유게시판"; break;
		   case "3" : boardName = "QNA"; break;
		}
		//게시판 조회 처리
		int limit = 10; //한 페이지 출력될 게시물 건수
		//listcount : boardid 별 전체 게시물 건수. 
		int listcount = service.boardcount(boardid,searchtype,searchcontent);
		//boardlist : 해당 페이지 출력될 게시물 목록
		List<Board> boardlist = service.boardlist
				          (pageNum,limit,boardid,searchtype,searchcontent);
		//페이징 처리를 위한 변수
		// 게시물 건수에 따른 최대 페이지값
		int maxpage = (int)((double)listcount/limit + 0.95);
		//startpage : 현재 화면에 보여질 시작 페이지 값
		int startpage = (int)((pageNum/10.0 + 0.9) - 1) * 10 + 1;
		//endpage : 현재 화면에 보여질 마지막 페이지 값
		int endpage = startpage + 9;
		//마지막 페이지 값은 최대 페이지 보다 클 수 없다
		if(endpage > maxpage) endpage = maxpage;
		//화면에 보여질 게시물 번호
		int boardno = listcount - (pageNum - 1) * limit;
		mav.addObject("boardid",boardid);  
		mav.addObject("boardName", boardName); 
		mav.addObject("pageNum", pageNum); 
		mav.addObject("maxpage", maxpage); 
		mav.addObject("startpage", startpage);
		mav.addObject("endpage", endpage); 
		mav.addObject("listcount", listcount);
		mav.addObject("boardlist", boardlist);
		mav.addObject("boardno", boardno);		
		return mav;
	}
	@GetMapping("detail")
	public ModelAndView detail(Integer num) {
		ModelAndView mav = new ModelAndView();
		Board board = service.getBoard(num); 
		service.addReadcnt(num);
		if(board.getBoardid() == null || board.getBoardid().equals("1"))
			mav.addObject("boardName","공지사항");
		else if(board.getBoardid().equals("2"))
			mav.addObject("boardName","자유게시판");
		else if(board.getBoardid().equals("3"))
			mav.addObject("boardName","QNA");
		mav.addObject("board",board);
		return mav;
	}
	@PostMapping("write")
	public ModelAndView writePost(@Valid Board board, BindingResult bresult,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if (bresult.hasErrors()) {
			return mav;
		}
		if(board.getBoardid() == null) board.setBoardid("1");
		service.boardWrite(board,request);
		mav.setViewName("redirect:list?boardid=" + board.getBoardid());
		return mav;
	}
	@GetMapping({"reply","update","delete"})
	public ModelAndView getBoard(Integer num,String boardid) {
		ModelAndView mav = new ModelAndView();
		Board board = service.getBoard(num); 
		mav.addObject("board",board);
		if(boardid == null || boardid.equals("1"))
			mav.addObject("boardName","공지시항");
		else if(boardid.equals("2"))
			mav.addObject("boardName","자유게시판");
		else if(boardid.equals("3"))
			mav.addObject("boardName","QNA");
		return mav;
	}
	@PostMapping("update")
	public ModelAndView update(@Valid Board board, BindingResult bresult,
			  HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			return mav;
		}
		Board dbBoard = service.getBoard(board.getNum());
		if(!board.getPass().equals(dbBoard.getPass())) {
			throw new ShopException
			     ("비밀번호가 틀립니다.",
			    "update?num="+board.getNum()+"&boardid="+dbBoard.getBoardid());			
		}
		//입력값 정상, 비밀번호 일치
		try {
			service.boardUpdate(board,request);
			mav.setViewName("redirect:detail?num="+board.getNum());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ShopException
			  ("게시글 수정에 실패 했습니다.",
			    "update?num="+board.getNum()+"&boardid="+dbBoard.getBoardid());			
		}
		return mav;
	}
	@PostMapping("delete")
	public ModelAndView delete(Board board, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		if(board.getPass() == null || board.getPass().trim().equals("")) {
			bresult.reject("error.input.password");
			return mav;
		}
		Board dbboard = service.getBoard(board.getNum());
		if(!board.getPass().equals(dbboard.getPass())) {
			bresult.reject("error.check.password");
			return mav;
		}
		try {
			service.boardDelete(board.getNum());
			mav.setViewName("redirect:list?boardid="+dbboard.getBoardid());
		} catch (Exception e) {
			e.printStackTrace();
			bresult.reject("error.board.delete");
		}
		return mav;
	}
	
}
