package kr.gdu.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
public class SaleDto {	//db의 sale 테이블의 내용 + 사용자정보 + 주문상품정보
	private int saleid;  //주문번호
	private String userid;  //주문 고객의 아이디
	private Date saledate;  //주문 일자
	private UserDto user;  //고객정보
	private List<SaleItemDto> itemList = new ArrayList<>(); //주문상품 목록
	public int getTotal() {
		return itemList.stream()
			 .mapToInt(s->s.getItem().getPrice() * s.getQuantity()).sum(); 
	}
}
