package kr.gdu.dto;

import java.util.Date;

import lombok.Data;

@Data
public class BoardDto {
	private int num;
	private String name;
	private String pass;
	private String subject;
	private String content;
	private String file1;
	private Date regdate;
	private int readcnt;
	private String boardid;
}
