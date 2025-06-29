package kr.gdu.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
public class MailDto {
	@NotEmpty(message="구글 아이디를 입력하세요")
	private String googleid;
	@NotEmpty(message="구글 비밀번호를 입력하세요")
	private String googlepw;
	private String recipient;
	@NotEmpty(message="제목을 입력하세요")
	private String title;
	private String mtype;
	private List<MultipartFile> file1; //메일 전송시 첨부파일 2개가능
	@NotEmpty(message="내용을 입력하세요")
	private String contents;
}
