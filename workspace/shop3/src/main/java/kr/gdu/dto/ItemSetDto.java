package kr.gdu.dto;

import kr.gdu.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor 
public class ItemSetDto {
	private Item item;  //상품
	private Integer quantity; //수량
}
