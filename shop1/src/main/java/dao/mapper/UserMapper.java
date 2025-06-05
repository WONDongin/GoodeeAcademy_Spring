package dao.mapper;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import logic.User;

public interface UserMapper {

	@Insert("insert into useraccount "
			+ "(userid,username,password,birthday,phoneno,postcode,address,email)"
			+ "values (#{userid},#{username},#{password},#{birthday},#{phoneno},#{postcode},#{address},#{email})")
	void insert(User user);

	@Select({"<script>",
	    "select * from useraccount ",
	    "<if test='userid != null'> where userid=#{userid}</if>",
	"</script>"})
	List<User> select(Map<String, Object> param);
	
}
