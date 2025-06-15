package kr.gdu.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.gdu.logic.Board;
@Mapper
public interface BoardMapper {
    String select = "select num,writer,pass,title,content,file1 fileurl,"
		+ " regdate, readcnt, grp, grplevel, grpstep, boardid from board";
    
    @Select({"<script>",
   	"select count(*) from board where boardid=#{boardid} ",
    "<if test='searchtype != null'> "
    + " and ${searchtype} like '%${searchcontent}%'</if>",
   	"</script>"})
	int count(Map<String, Object> param);
    @Select({"<script>",
    	select,
   	"<if test='num != null'> where num = #{num}</if>",
   	"<if test='boardid != null'> where boardid = #{boardid} </if>",
   	"<if test='searchtype != null'> "
   	+ " and ${searchtype} like '%${searchcontent}%'</if>",
   	"<if test='limit != null'> "
   	+ " order by grp desc, grpstep asc limit #{startrow}, #{limit}</if>",
   	"</script>"})    
	List<Board> select(Map<String, Object> param);

    @Update("update board set readcnt = readcnt + 1 where num=#{num}")
	void addReadcnt(Map<String, Object> param);
    
    @Select("select ifnull(max(num),0) from board")
	int maxNum();

    @Insert("insert into board "
   		+ "(num,boardid,writer,pass,title,content,file1, "
		+ "regdate,readcnt,grp,grplevel,grpstep) values (" 
		+ "#{num},#{boardid},#{writer},#{pass},#{title},#{content},#{fileurl}, "
		+ "now(),0,#{grp},#{grplevel},#{grpstep})")
	void insert(Board board);
    
    @Update("update board set writer=#{writer},title=#{title},content=#{content},"
			 + " file1=#{fileurl} where num=#{num}")
	void update(Board board);

    @Delete("delete from board where num = #{num}")
	void delete(int num);
}
