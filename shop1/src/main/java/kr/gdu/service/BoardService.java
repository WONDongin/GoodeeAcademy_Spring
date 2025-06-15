package kr.gdu.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.gdu.dao.BoardDao;
import kr.gdu.logic.Board;

@Service
public class BoardService {
	@Autowired
	BoardDao boardDao;
	
	public int boardcount(String boardid, String searchtype, String searchcontent) {
		return boardDao.count(boardid,searchtype,searchcontent);
	}
	public List<Board> boardlist
	(Integer pageNum, int limit, String boardid, String searchtype, String searchcontent) {
		return boardDao.list(pageNum,limit,boardid,searchtype,searchcontent);
	}
	public Board getBoard(Integer num) {
		return boardDao.selectOne(num);  //board 레코드 조회
	}
	public void addReadcnt(Integer num) {
		boardDao.addReadcnt(num);       //조회수 증가
	}
	public void boardWrite(Board board, HttpServletRequest request) {
		int maxnum = boardDao.maxNum();
		board.setNum(++maxnum);
		board.setGrp(maxnum);
		if(board.getFile1() != null && !board.getFile1().isEmpty()) {
			String path = 
					request.getServletContext().getRealPath("/") + "board/file/";
			this.uploadFileCreate(board.getFile1(), path);
			board.setFileurl(board.getFile1().getOriginalFilename());
		}
		boardDao.insert(board);		
	}
	public void uploadFileCreate(MultipartFile file, String path) {
		String orgFile = file.getOriginalFilename();
		File f = new File(path);
		if(!f.exists()) f.mkdirs();
		try {
			file.transferTo(new File(path+orgFile));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void boardUpdate(Board board, HttpServletRequest request) {
		if(board.getFile1() != null && !board.getFile1().isEmpty()) {
			String path = request.getServletContext().getRealPath("/")
					                                    + "board/file/";
			this.uploadFileCreate(board.getFile1(), path);  
			board.setFileurl(board.getFile1().getOriginalFilename());
		}
		boardDao.update(board);
	}
	public void boardDelete(int num) {
		boardDao.delete(num);
	}
}
