package kr.gdu.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.gdu.dao.mapper.BoardMapper;
import kr.gdu.logic.Board;

@Repository
public class BoardDao {
	@Autowired
	private SqlSessionTemplate template;
	private Class<BoardMapper> cls = BoardMapper.class;
	private Map<String,Object> param = new HashMap<>();
	
	public int count(String boardid, String searchtype, String searchcontent) {
		param.clear();
		param.put("boardid", boardid);
		param.put("searchtype",searchtype);
		param.put("searchcontent",searchcontent);
		return template.getMapper(cls).count(param);
	}	
	public List<Board> list	(Integer pageNum, int limit, 
			String boardid, String searchtype, String searchcontent) {
		param.clear();
		param.put("startrow", (pageNum - 1) * limit); 
		param.put("limit",  limit);		
		param.put("boardid",  boardid);		
		param.put("searchtype",searchtype);
		param.put("searchcontent",searchcontent);
		return template.getMapper(cls).select(param);
	}
	public Board selectOne(Integer num) {
		param.clear();
		param.put("num", num);
		return template.selectOne("kr.gdu.dao.mapper.BoardMapper.select",param);
	}
	public void addReadcnt(Integer num) {
		param.clear();
		param.put("num", num);
		template.getMapper(cls).addReadcnt(param);
	}
	public int maxNum() {
		return template.getMapper(cls).maxNum();
	}
	public void insert(Board board) {
		template.getMapper(cls).insert(board);
	}
	public void update(Board board) {
		template.getMapper(cls).update(board);		
	}
	public void delete(int num) {
		template.getMapper(cls).delete(num);
	}
	
	// 답변기능
	public void grpStepAdd(Board board) {
	    Map<String, Object> param = new HashMap<>();
	    param.put("grp", board.getGrp());
	    param.put("grpstep", board.getGrpstep());
	    template.getMapper(cls).grpStepAdd(param);
	}


}
