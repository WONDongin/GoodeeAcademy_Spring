package kr.gdu.dto;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import kr.gdu.domain.User;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class UserDto {
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
	public UserDto(User dom) {
		this.userid = dom.getUserid();
		this.password = dom.getPassword();
		this.username = dom.getUsername();
		this.phoneno = dom.getPhoneno();
		this.postcode = dom.getPostcode();
		this.address = dom.getAddress();
		this.email = dom.getEmail();
		this.birthday = dom.getBirthday();
	}
}
