package kr.gdu.dao.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import kr.gdu.logic.Sale;
@Mapper
public interface SaleMapper {
	@Select("select ifnull(max(saleid),0) from sale")
	int maxid();

	@Insert("insert into sale (saleid,userid,saledate) "
			+ " values(#{saleid},#{userid},now())")
	void insert(Sale sale);

	@Select("select * from sale where userid= #{value}")
	List<Sale> list(String userid);
}
