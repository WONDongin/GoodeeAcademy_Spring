package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.ItemMapper; // 인터페이스로 생성
import logic.Item;

@Repository // @Component + DAO 관련 기능. 객체화
public class ItemDao {
	@Autowired // SqlSessionTemplate 객체를 주입
	private SqlSessionTemplate template;
	
	private Map<String, Object> param = new HashMap<>();
	
	// 상품 리스트
	private final Class<ItemMapper> cls = ItemMapper.class;
	public List<Item> list(){
		return template.getMapper(cls).select(null);
	}
	
	// 상품 상세 보기
	public Item select(Integer id) {
		param.clear();
		param.put("id", id);
		return template.getMapper(cls).select(param).get(0); // get(0) : 첫번째 있는
	}

	public int maxId() {
		return template.getMapper(cls).maxId();
	}

	public void insert(Item item) {
		template.getMapper(cls).insert(item);
	}
	
	// 상품 수정
	public void update(Item item) {
		template.getMapper(cls).update(item);
	}

	// 상품 삭제
	public void delete(Integer id) {
		template.getMapper(cls).delete(id);
	}
}
