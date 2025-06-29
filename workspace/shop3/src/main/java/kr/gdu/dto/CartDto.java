package kr.gdu.dto;

import java.util.ArrayList;
import java.util.List;

public class CartDto {
	private List<ItemSetDto> itemSetList = new ArrayList<>();
	public List<ItemSetDto> getItemSetList() {
		return itemSetList;
	}
	public void push(ItemSetDto itemSet) {
		//itemSet : 추가될 item
		int count = itemSet.getQuantity(); //추가될 수량
		for(ItemSetDto old : itemSetList) {
			//old : 추가되어 있는 ItemSet 객체
			if(itemSet.getItem().getId() == old.getItem().getId()) {
				count = old.getQuantity() + itemSet.getQuantity();
				old.setQuantity(count);
				return;
			}
		}
	   itemSetList.add(itemSet);
	}
	public int getTotal() { //get프로퍼티 : total 프로퍼티
		return itemSetList.stream()
				.mapToInt(s->s.getItem().getPrice() * s.getQuantity())
				.sum();
	}
}
