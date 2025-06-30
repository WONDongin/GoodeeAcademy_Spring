package kr.gdu.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import kr.gdu.domain.Board;
import kr.gdu.domain.Comment;
import kr.gdu.dto.BoardDto;
import kr.gdu.dto.CommentDto;
import kr.gdu.exception.ShopException;
import kr.gdu.service.BoardService;

@Controller
@RequestMapping("board")
public class BoardController {
	@Autowired
	private BoardService service;
	
	@GetMapping("*")
	public ModelAndView write() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new BoardDto());
		return mav;
	}
	/* Spring에서 파라미터 전달 방식
	 *  - JPA를 이용한 페이징 처리
	 *  - JPA를 이용한 동적쿼리. - 게시판 검색부분 
	 */
	@RequestMapping("list")
	public ModelAndView list(@RequestParam Map<String,String> param,
			 HttpSession session) {
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
		Page<Board> boardlist = service.boardlist
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
		mav.addObject("today",
				new SimpleDateFormat("yyyyMMdd").format(new Date()));
		return mav;
	}
	@GetMapping("detail")
	public ModelAndView detail(Integer num) {
		ModelAndView mav = new ModelAndView("board/detail");
		Board board = service.getBoard(num); 
		service.addReadcnt(num);
		if(board.getBoardid() == null || board.getBoardid().equals("1"))
			mav.addObject("boardName","공지사항");
		else if(board.getBoardid().equals("2"))
			mav.addObject("boardName","자유게시판");
		else if(board.getBoardid().equals("3"))
			mav.addObject("boardName","QNA");
		
	    //댓글 목록 화면에 전달
		//commlist : num 게시물의 댓글 목록
	    List<Comment> commlist = service.commentlist(num);
		//유효성 검증에 필요한 Comment 객체
		CommentDto comm = new CommentDto();
		comm.setNum(num);
		mav.addObject("board",board);
		mav.addObject("commlist",commlist);
		mav.addObject(comm);
		return mav;
	}
	@PostMapping("write")
	public ModelAndView writePost(@Valid BoardDto board, BindingResult bresult,
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
	//게시판 : 답변, 수정, 삭제
	@GetMapping({"reply","update","delete"})
	public ModelAndView getBoard(Integer num,String boardid) {
		ModelAndView mav = new ModelAndView();
		Board board = service.getBoard(num); 
		mav.addObject("boardDto",new BoardDto(board));
		if(boardid == null || boardid.equals("1"))
			mav.addObject("boardName","공지사항");
		else if(boardid.equals("2"))
			mav.addObject("boardName","자유게시판");
		else if(boardid.equals("3"))
			mav.addObject("boardName","QNA");
		return mav;
	}
	@PostMapping("update")
	public ModelAndView update(@Valid BoardDto board, BindingResult bresult,
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
	public ModelAndView delete(BoardDto board, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		if(board.getPass() == null || board.getPass().trim().equals("")) {
			bresult.reject("error.input.password");
			return mav;
		}
		Board dbboard = service.getBoard(board.getNum());
		//비밀번호 검증
		//board.getPass() : 화면 입력 비밀번호
		//dbboard.getPass() : db에 등록된 비밀번호
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
	/*
	 * 1. 유효성 검사하기-파라미터값 저장. 
	 *     - 원글정보 : num,grp,grplevel,grpstep,boardid
	 *     - 답글정보 : writer,pass,title,content
	 * 2. db에 insert => service.boardReply()
	 *     - 원글의 grpstep 보다 큰 기존 등록된 답글의 grpstep 값을 +1 수정 
	 *       => boardDao.grpStepAdd()
	 *     - num : maxNum() + 1  
	 *     - db에 insert  => boardDao.insert()
	 *       grp : 원글과 동일
	 *       grplevel : 원글의 grplevel + 1    
	 *       grpstep : 원글의 grpstep + 1
	 * 3. 등록 성공 : list로 페이지 이동
	 *    등록 실패 : "답변 등록시 오류 발생" reply 페이지 이동           
	 */		
	@PostMapping("reply")
	public ModelAndView reply(@Valid BoardDto board, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		//유효성 검증
    	if(bresult.hasErrors()) {
			return mav;
    	}
    	try {
     	   service.boardReply(board);
     	   mav.setViewName("redirect:list?boardid="+board.getBoardid());
     	} catch(Exception e) {
     		e.printStackTrace();
     		String url = "reply?num="+board.getNum()+
     				    "&boardid=" + board.getBoardid();
     		throw new ShopException("답변등록시 오류 발생",url);
     	}
 	    return mav;    	
	}
	@RequestMapping("comment")  //댓글 등록
	public ModelAndView comment(@Valid CommentDto comm,BindingResult bresult) {
		ModelAndView mav = new ModelAndView("board/detail");
		if(bresult.hasErrors()) {
			//입력 오류시, 정상적으로 조회 되도록 수정
			return commdetail(comm);
		}
		//comment 테이블의 기본키값: num,seq
		int seq = service.commmaxseq(comm.getNum()); //num 게시글 중 최대 seq값
		comm.setSeq(++seq);
		service.comminsert(new Comment(comm)); //comment 테이블에 추가
		mav.setViewName("redirect:detail?num="+comm.getNum()+"#comment");
		return mav;		
	}
	private ModelAndView commdetail(CommentDto comm) {
		ModelAndView mav = detail(comm.getNum()); //조회수가 증가됨. 수정 필요
		//comm : @Valid 완료한 객체. 오류 정보 저장
		mav.addObject(comm);
		mav.setViewName("board/detail");
		return mav;
	}
	@RequestMapping("commdel")
	public String commdel(CommentDto comm) {
		Comment dbcomm = service.commSelectOne(comm.getNum(),comm.getSeq());
		if(comm.getPass().equals(dbcomm.getPass())) {
			service.commdel(comm.getNum(),comm.getSeq());
		} else {
			throw new ShopException("비밀번호가 틀립니다.",
					"detail?num="+comm.getNum()+"#comment");
		}
		return "redirect:detail?num="+comm.getNum()+"#comment";
	}
}
