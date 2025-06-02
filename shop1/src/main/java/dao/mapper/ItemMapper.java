package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

import logic.Item;

public interface ItemMapper {
	@Select({"<script>",
	"select * from item <if test='id != null'>where id=#{id}</if> order by id",
	"</script>"})
	List<Item> select(Map<String, Object> param);
}
