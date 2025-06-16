package kr.gdu.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.gdu.dao.mapper.UserMapper;
import kr.gdu.logic.User;

@Repository
public class UserDao {
	@Autowired
	private SqlSessionTemplate template;
	private Map<String,Object> param = new HashMap<>();
	private Class<UserMapper> cls = UserMapper.class;
	
	public void insert(User user) {
		template.getMapper(cls).insert(user);
	}

	public User selectOne(String userid) {
		param.clear();
		param.put("userid", userid);
		return template.selectOne
				("kr.gdu.dao.mapper.UserMapper.select",param);
	}

	public void update(User user) {
		 template.getMapper(cls).update(user);
	}

	public void delete(String userid) {
		 template.getMapper(cls).delete(userid);
	}

	public void chgpass(String userid, String chgpass) {
		template.getMapper(cls).chgpass(userid,chgpass);
	}

	public String search(User user) {
		String col = "userid";                         //아이디 검색
		if(user.getUserid() != null) col = "password"; //비밀번호 검색
		param.clear();
		param.put("col", col);
		param.put("userid", user.getUserid());
		param.put("email", user.getEmail());
		param.put("phoneno", user.getPhoneno());
		return template.getMapper(cls).search(param);
	}

	public List<User> list() {
		return template.getMapper(cls).select(null);
	}
}
