package kr.gdu.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import kr.gdu.dao.mapper.ItemMapper; //인터페이스로 생성
import kr.gdu.logic.Item;

@Repository   //@Component + DAO관련기능. 객체화
public class ItemDao {
	@Autowired   //SqlSessionTemplate 객체 주입 
	private SqlSessionTemplate template;
	private Map<String, Object> param = new HashMap<>();
	
	private final Class<ItemMapper> cls = ItemMapper.class;
	public List<Item> list() {
		return template.getMapper(cls).select(null);  
	}
	public Item select(Integer id) {
		param.clear();
		param.put("id", id);
		return template.getMapper(cls).select(param).get(0);  
	}
	public int maxId() {
		return template.getMapper(cls).maxId();
	}
	public void insert(Item item) {
		template.getMapper(cls).insert(item);
	}
	public void update(Item item) {
		template.getMapper(cls).update(item);
	}
	public void delete(Integer id) {
		template.getMapper(cls).delete(id);
	}
}
