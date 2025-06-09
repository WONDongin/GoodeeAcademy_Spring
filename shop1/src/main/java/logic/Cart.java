package logic;

import java.util.ArrayList;
import java.util.List;

public class Cart {
	private List<ItemSet> itemSetList = new ArrayList<>();
	public List<ItemSet> getItemSetList(){
		return itemSetList;
	}
	// 상품 아이템 추가
	public void push(ItemSet itemSet) {
		itemSetList.add(itemSet);
	}
	// 전체 상품의 가격 : (가격 * 수량)의 합
	public int getTotal() { // get 프로퍼티 : total 프로퍼티
		return itemSetList.stream().mapToInt(s -> s.getItem().getPrice() * s.getQuantity()).sum();
	}
}
