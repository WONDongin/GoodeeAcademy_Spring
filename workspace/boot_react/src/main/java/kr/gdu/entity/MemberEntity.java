package kr.gdu.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.gdu.dto.MemberDto;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity 
@Data
@NoArgsConstructor
public class MemberEntity {
	@Id
	private String id;
	private String pass;
	private String name;
	private int gender;
	private String tel;
	private String email;
	private String picture;
	public MemberEntity(MemberDto dto) {
		this.id = dto.getId();
		this.pass = dto.getPass();
		this.name = dto.getName();
		this.gender = dto.getGender();
		this.tel = dto.getTel();
		this.email = dto.getEmail();
		this.picture = dto.getPicture();
	}
}
