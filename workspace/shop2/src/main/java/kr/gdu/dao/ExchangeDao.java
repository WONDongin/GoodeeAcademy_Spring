package kr.gdu.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.gdu.dao.mapper.ExchangeMapper;
import kr.gdu.logic.Exchange;

@Repository
public class ExchangeDao {
	@Autowired
	private SqlSessionTemplate template;
	private Class<ExchangeMapper> cls = ExchangeMapper.class;
	
	public void insert(Exchange ex) {
		template.getMapper(cls).insert(ex);
	}
}
