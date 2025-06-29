package kr.gdu.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import kr.gdu.dto.ItemDto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.NoArgsConstructor;
//JPA는 모든 테이블에 기본키 필수
@Entity
@Table(name="item")
@Data
@NoArgsConstructor
public class Item {  //영속객체
	@Id    //id 컬럼이 기본키임.
	private int id;
	private String name;
	private int price;
	private String description;
	private String pictureUrl;
	@Transient  //컬럼과 무관한 프로퍼티
	private MultipartFile picture; //업로드된 파일 저장
	public Item(ItemDto item) {
		this.id = item.getId();
		this.name = item.getName();
		this.price = item.getPrice();
		this.description = item.getDescription();
		this.pictureUrl = item.getPictureUrl();
	}
}
