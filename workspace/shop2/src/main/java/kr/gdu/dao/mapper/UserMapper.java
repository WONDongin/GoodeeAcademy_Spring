package kr.gdu.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.gdu.logic.User;
@Mapper
public interface UserMapper {

	@Insert("insert into useraccount "
	+ "(userid,username,password,birthday,phoneno,postcode,address,email)"
	+ " values (#{userid},#{username},#{password},#{birthday},"
	+ "#{phoneno},#{postcode},#{address},#{email})")
	void insert(User user);

	@Select({"<script>",
		  "select * from useraccount ",
		  "<if test='userid != null'> where userid=#{userid}</if>",
		  "</script>"})
	List<User> select(Map<String,Object> param);

	@Update("update useraccount set username=#{username},"
			+ "birthday=#{birthday},phoneno=#{phoneno},postcode=#{postcode},"
			+ "address=#{address},email=#{email} where userid=#{userid}")
	void update(User user);

	@Delete("delete from useraccount where userid=#{value}")
	void delete(String userid);

	@Update("update useraccount set password=#{chgpass} "
			+ " where userid=#{userid}")
	void chgpass(@Param("userid") String userid,
			     @Param("chgpass")  String chgpass);

	@Select({"<script>",
		"select ${col} from useraccount "
		+ "where email=#{email} and phoneno=#{phoneno} "
		+ "<if test='userid != null'> and userid=#{userid}</if>",
		"</script>"})
	String search(Map<String, Object> param);
}
