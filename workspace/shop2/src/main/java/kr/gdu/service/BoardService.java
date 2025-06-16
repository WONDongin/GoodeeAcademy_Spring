package kr.gdu.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.gdu.dao.BoardDao;
import kr.gdu.dao.CommDao;
import kr.gdu.logic.Board;
import kr.gdu.logic.Comment;

@Service
public class BoardService {
	// 환경변수값 읽기
	@Value("${summernote.imgupload}")
	private String UPLOAD_IMAGE_DIR;
	@Autowired
	BoardDao boardDao;
	@Autowired
	CommDao commDao;
	
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
	
	// 답글기능
	public void boardReply(Board board) {
		boardDao.grpStepAdd(board);  // 이미 등록된 grpstep값 1씩 증가
		int max = boardDao.maxNum(); // 최대 num 조회
		board.setNum(++max); // 원글의 num => 답변글의 num 값으로 변경
							 // 원글의 grp => 답변글의 grap 값을 동일. 설정필요 없음
							 // 원글의 boardid => 답변글의 boardid 값을 동일. 설정
		board.setGrplevel(board.getGrplevel() + 1); // 원글의 grplevel => +1 답변
		board.setGrpstep(board.getGrpstep() + 1);   // 원글의 getGrpstep => +1 답변
		boardDao.insert(board);
	}
	// 댓글기능
	public List<Comment> commentList(Integer num) {
		return commDao.list(num);
	}
	public int commmaxseq(int num) {
		return commDao.maxseq(num);
	}
	public void comminsert(@Valid Comment comm) {
		commDao.insert(comm);
	}
	// 삭제할 댓글 선택
	public Comment commSelectOne(int num, int seq) {
		return commDao.selectOne(num,seq);
	}
	// 댓글삭제
	public void commdel(int num, int seq) {
		commDao.delete(num, seq);
	}
	// 이미지 업로드
	public String summernoteImageUpload(MultipartFile multipartFile) {
		File dir = new File(UPLOAD_IMAGE_DIR + "board/image");
		if(!dir.exists()) dir.mkdirs();
		String filesystemName = multipartFile.getOriginalFilename();
		File file = new File(dir,filesystemName);
		try {
			multipartFile.transferTo(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/board/image/" + filesystemName; // url 리턴
	}
}
