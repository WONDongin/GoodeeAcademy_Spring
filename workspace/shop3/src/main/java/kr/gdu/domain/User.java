package kr.gdu.domain;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import kr.gdu.dto.UserDto;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name="Usercipher")
@Table(name="usercipher")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
	@Id
	private String userid;
	@Transient
	private String channel;     
	private String password;
	private String username;
	private String phoneno;
	private String postcode;
	private String address;
	private String email;
	private Date birthday;
	public User(UserDto dto) {
		this.userid = dto.getUserid();
		this.password = dto.getPassword();
		this.username = dto.getUsername();
		this.phoneno = dto.getPhoneno();
		this.postcode = dto.getPostcode();
		this.address = dto.getAddress();
		this.email = dto.getEmail();
		this.birthday = dto.getBirthday();
	}
}
