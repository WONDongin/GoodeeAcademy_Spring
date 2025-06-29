package kr.gdu.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.gdu.dto.ItemSetDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="saleitem")
//@IdClass : 키의 클래스. 다중키에서 사용됨
@IdClass(SaleItemId.class)  //기본키가 복수개인 경우 필요
@Setter
@Getter
@ToString
@NoArgsConstructor  //매개변수 없는 생성자
public class SaleItem {
	@Id
	private int saleid;  //주문번호
	@Id
	private int seq;     //주문상품번호
	private int itemid;  //상품아이디
	private int quantity;//주문상품수량
	@ManyToOne
	private Item item;   //상품아이디에 해당하는 상품정보
	public SaleItem(int saleid, int seq, ItemSetDto itemSet) {
		this.saleid = saleid;
		this.seq = seq;
		this.item = itemSet.getItem();
		this.itemid = itemSet.getItem().getId(); //상품id
		this.quantity = itemSet.getQuantity();   //주문수량
	}
}
