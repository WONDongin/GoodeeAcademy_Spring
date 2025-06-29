package kr.gdu.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Entity
@Table(name="sale")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Sale {	//db의 sale 테이블의 내용 + 사용자정보 + 주문상품정보
	@Id
	private int saleid;  //주문번호
	private String userid;  //주문 고객의 아이디
	@Temporal(TemporalType.TIMESTAMP) //날짜+시간
	private Date saledate;  //주문 일자
	@ManyToOne
	private User user;  //고객정보
	@OneToMany
	private List<SaleItem> itemList = new ArrayList<>(); //주문상품 목록
	@PrePersist
	public void onPrePersist() { //save() 함수 호출직전에 먼저호출
		this.saledate = new Date();
	}
	public int getTotal() {
		return itemList.stream()
			 .mapToInt(s->s.getItem().getPrice() * s.getQuantity()).sum(); 
	}
/*
 * @OneToOne : 1:1 매핑관계	
 * @OneToMany : 1 : n의 관계
 * @ManyToOne : n : 1의 관계. 사용자 와 주문 테이블
 * @ManyToMany : n:m의 관계. 학생과 수업테이블
 */
}
