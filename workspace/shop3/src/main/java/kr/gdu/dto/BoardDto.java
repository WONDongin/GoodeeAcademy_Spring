package kr.gdu.dto;

import java.util.Date;

import jakarta.validation.constraints.NotEmpty;
import kr.gdu.domain.Board;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardDto {
	private int num;
	private String boardid;
	@NotEmpty(message="글쓴이를 입력하세요")
	private String writer;
	@NotEmpty(message="비밀번호를 입력하세요")
	private String pass;
	@NotEmpty(message="제목을 입력하세요")
	private String title;
	@NotEmpty(message="내용을 입력하세요")
	private String content;
	private MultipartFile file1;
	private String fileurl; //파일 이름
	private Date regdate;
	private int readcnt;
	private int grp;
	private int grplevel;
	private int grpstep;

	public BoardDto(Board dm) {
		this.num = dm.getNum();
		this.boardid = dm.getBoardid();
		this.writer = dm.getWriter();
		this.pass = dm.getPass();
		this.title = dm.getTitle();
		this.content = dm.getContent();
		this.fileurl = dm.getFile1();
		this.regdate = dm.getRegdate();
		this.readcnt = dm.getReadcnt();
		this.grp = dm.getGrp();
		this.grplevel = dm.getGrplevel();
		this.grpstep = dm.getGrpstep();
	}
	
}
