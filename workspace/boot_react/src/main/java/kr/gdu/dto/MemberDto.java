package kr.gdu.dto;

import lombok.Data;

@Data
public class MemberDto {
	private String id;
	private String pass;
	private String name;
	private int gender;
	private String tel;
	private String email;
	private String picture;
}
