package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dao.UserDao;
import logic.User;

@Service
public class UserService {
	@Autowired
	UserDao userdao;
	
	public void userInsert(User user) {
		userdao.insert(user);
	}

	public User selectUser(String userid) {
		return userdao.selectOne(userid);
	}
}
