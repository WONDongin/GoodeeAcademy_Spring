package kr.gdu.dto;

import java.util.Date;

import jakarta.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
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
}
