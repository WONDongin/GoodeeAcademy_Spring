package kr.gdu.logic;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class User {
	@Size(min=3,max=10,message="아이디는 3자이상 10자이하로 입력하세요")
	private String userid;
	private String channel;     
	@Size(min=3,max=10,message="비밀번호는 3자이상 10자이하로 입력하세요")
	private String password;
	@NotEmpty(message="사용자이름은 필수 입니다.")
	private String username;
	private String phoneno;
	private String postcode;
	private String address;
	@NotEmpty(message="email을 입력하세요.")
	@Email(message="email 형식으로 입력하세요")
	private String email;
//	@NotNull(message="생일을 입력하세요") //필수 입력
	@Past(message="생일은 과거 날짜만 가능합니다.")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date birthday;
}
