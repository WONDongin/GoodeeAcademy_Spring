package kr.gdu.logic;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

// JPA는 모든 테이블에 기본키 필수
@Entity
@Table(name="item") 
@Data
public class Item { // 영속객체
	@Id // id 칼럼이 기본키임
	private int id;
	//@NotEmpty : null 또는 공백인 경우 오류 인식
	@NotEmpty(message="상품명을 입력하세요")
	private String name;
	@Min(value=10,message="10원이상 가능합니다")
	@Max(value=100000,message="10만원이하만 가능합니다")
	private int price;
	@NotEmpty(message="상품설명을 입력하세요")
	private String description;
	private String pictureUrl;
	@Transient // 컬럼과 무관한 프로퍼티
	private MultipartFile picture; //업로드된 파일 저장
}
