package kr.gdu.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.gdu.dao.mapper.CommentMapper;
import kr.gdu.logic.Comment;

@Repository
public class CommDao {
	@Autowired
	private SqlSessionTemplate template;
	private Map<String,Object> param = new HashMap<>();
	private Class<CommentMapper> cls = CommentMapper.class;
	// 댓글 기능
	public List<Comment> list(Integer num) {
		return template.getMapper(cls).list(num);
	}
	public int maxseq(int num) {
		return template.getMapper(cls).maxseq(num);
	}
	public void insert(Comment comm) {
		 template.getMapper(cls).insert(comm);
	}
	// 삭제할 댓글 선택
	public Comment selectOne(int num, int seq) {
		return  template.getMapper(cls).selectOne(num, seq);
	}
	// 댓글삭제
	public void delete(int num, int seq) {
		template.getMapper(cls).delete(num, seq);
	}
}
