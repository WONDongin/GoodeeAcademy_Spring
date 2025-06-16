package kr.gdu.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import kr.gdu.logic.Comment;

@Mapper
public interface CommentMapper {
	// 댓글기능
	@Select("select * from comment where num=#{num} order by seq desc")
	List<Comment> list(Integer num);
	
	@Select("select ifnull(max(seq),0) from comment where num=#{num}")
	int maxseq(int num);
	
	@Insert("insert into comment (num,seq,writer,pass,content,regdate) "
			+ " values (#{num},#{seq},#{writer},#{pass},#{content},now())")
	void insert(Comment comm);
	
	// 삭제할 댓글 선택
    @Select("select * from comment where num=#{num} and seq=#{seq}")
    Comment selectOne(@Param("num") int num, @Param("seq") int seq);
    // 댓글 삭제
	@Delete("delete from comment where num=#{num} and seq=#{seq} ")
	void delete(int num, int seq);
}
