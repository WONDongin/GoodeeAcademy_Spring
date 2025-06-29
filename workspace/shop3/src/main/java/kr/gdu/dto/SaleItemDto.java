package kr.gdu.dto;

import kr.gdu.domain.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor  //매개변수 없는 생성자
public class SaleItemDto {
	private int saleid;  //주문번호
	private int seq;     //주문상품번호
	private int itemid;  //상품아이디
	private int quantity;//주문상품수량
	private Item item;   //상품아이디에 해당하는 상품정보
	public SaleItemDto(int saleid, int seq, ItemSetDto itemSet) {
		this.saleid = saleid;
		this.seq = seq;
		this.item = itemSet.getItem();
		this.itemid = itemSet.getItem().getId(); //상품id
		this.quantity = itemSet.getQuantity();   //주문수량
	}
}
