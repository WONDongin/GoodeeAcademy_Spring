package kr.gdu.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import kr.gdu.dto.BoardDto;
import kr.gdu.entity.BoardEntity;
import kr.gdu.service.BoardService;

@RestController
@RequestMapping("/board/")
@CrossOrigin(origins="http://localhost:3000", allowCredentials="true")
//allowCredentials="true" : 인증의 요청도 허용
public class BoardController {
	@Value("${image.upload.dir}")
	private String PATH;
	@Autowired
	BoardService service;
	
	@GetMapping("boardList")
	public Map<String,Object> boardList(@RequestParam Map<String,String> param) {
		Integer pageInt = null;
		for(String key : param.keySet()) {
			if(param.get(key) == null || param.get(key).trim().equals("")) {
				param.put(key, null);
			}
		}
		if(param.get("page") != null) {
			pageInt = Integer.parseInt(param.get("page"));
		} else {
			pageInt = 1;
		}
		String boardid = param.get("boardid");
		if(boardid==null) boardid="1";
		String boardName = null;
		switch(boardid) {
		case "1" : boardName="공지사항";break;
		case "2" : boardName="자유게시판";break;
		case "3" : boardName="QNA";break;
		}
		int limit = 10;
		int listcount =service.boardCount(boardid); //전체 게시물 등록건수
		//해당 페이지에 출력할 게시물목록
		List<BoardEntity> blist = service.boardList(pageInt,limit,boardid).getContent();

		//게시판 페이징에 필요한 변수 설정
		int bottomLine = 10; //한페이지 보여질 페이지번호 갯수  
		int start = (pageInt - 1) / bottomLine * bottomLine + 1; //시작 페이지 번호
		int end = start + bottomLine - 1;  //마지막 페이지 번호
		int maxpage = (listcount / limit) + (listcount % limit == 0 ? 0 : 1); //최대 페이지
		if (end > maxpage) end = maxpage;
		/*
		 * Map 객체 생성 
		 *  1.  Map <k,v> map = new HashMap<>();
		 *      map.put("k","v")....
		 *  2.  Map.of(k1,v1,k2,v2,k3,v3,....)
		 *      Java 9에 추가됨.
		 *      변경 불가. 
		 *      불변적인 성격  
		 *      최대 10까지 지원
		 */
		return Map.of(
				"boardid",boardid,
				"boardName",boardName,
				"pageInt",pageInt,
				"maxpage",maxpage,
				"start",start,
				"end",end,
				"listcount",listcount,
				"blist",blist,
				"bottomLine",bottomLine);
	}
	@PostMapping("boardPro")
	public BoardEntity boardPro(@RequestParam(value="file2", required = false) 
	MultipartFile multipartFile, BoardDto boardDto)  {
		String path = PATH+"img/board/";
		File dir = new File(path);
		if(!dir.exists()) dir.mkdirs();
		String filename="";
		if (multipartFile!=null && !multipartFile.isEmpty()) {
			File file = new File(path, multipartFile.getOriginalFilename());
			filename=multipartFile.getOriginalFilename();
			try {
				multipartFile.transferTo(file);
			} catch (Exception e) {
				e.printStackTrace();
		    }
		}
		boardDto.setBoardid(boardDto.getBoardid());
		boardDto.setFile1(filename);
		BoardEntity num = service.insertBoard(new BoardEntity(boardDto));
		return num;
	  }
	@GetMapping("boardInfo")
	public Map<String, Object> boardInfo(int num,HttpServletRequest request)  {
		System.out.println(request.getServletContext().getRealPath("/"));
		BoardEntity board = service.getBoard(num);
		service.addReadcnt(num);
		String boardName = null;
		if(board.getBoardid() == null || board.getBoardid().equals("1"))
			boardName = "공지사항";
		else if(board.getBoardid().equals("2"))
			boardName = "자유게시판";
		else if(board.getBoardid().equals("3"))
			boardName = "QNA";
		
		return	Map.of("board",board,
				"boardName",boardName);
	}	
	@GetMapping("boardUpdateForm")
	public Map<String, Object> boardUpdateForm(int num)  {
		BoardEntity board = service.getBoard(num);
		String boardName = null;
		if(board.getBoardid() == null || board.getBoardid().equals("1"))
			boardName = "공지사항";
		else if(board.getBoardid().equals("2"))
			boardName = "자유게시판";
		else if(board.getBoardid().equals("3"))
			boardName = "QNA";
		
		return	Map.of("board",board,
				"boardName",boardName);
	}	
	@PostMapping("boardUpdatePro")
	public Map<String,Object> boardUpdatePro(
	@RequestParam(value="file2", required = false) MultipartFile multipartFile, 
	                        BoardDto boardDto) throws IllegalStateException, IOException {
		BoardEntity dbBoard = service.getBoard(boardDto.getNum());
		Map<String,Object> map = new HashMap<>();
		if(!boardDto.getPass().equals(dbBoard.getPass())) {
			map.put("msg", "비밀번호 오류");
			map.put("code", 100);
			return map;
		}
		//입력값 정상, 비밀번호 일치
		String path =PATH+"img/board/";
		File dir = new File(path);
		if(!dir.exists()) dir.mkdirs();		
		String filename="";
		if (multipartFile != null && !multipartFile.isEmpty()) {
			File file = new File(path, multipartFile.getOriginalFilename());
			filename=multipartFile.getOriginalFilename();
			multipartFile.transferTo(file);	
			boardDto.setFile1(filename);
		} else {
			boardDto.setFile1(dbBoard.getFile1());
		}
		try {
		    boardDto.setRegdate(dbBoard.getRegdate());
			service.boardUpdate(new BoardEntity(boardDto));
			map.put("msg", "게시글 수정완료");
			map.put("code", 0);
		} catch (Exception e) {
			map.put("msg", "게시글 수정에 실패 했습니다");
			map.put("code", 200);
			e.printStackTrace();
		}
		return map;
	}
	
}
