package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.ItemDao;
import logic.Item;

@Service
public class ShopService {
	@Autowired
	private ItemDao itemDao;
	public List<Item> itemList(){
		return itemDao.list();
	}
}
