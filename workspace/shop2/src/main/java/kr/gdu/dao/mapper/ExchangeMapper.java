package kr.gdu.dao.mapper;

import org.apache.ibatis.annotations.Insert;

import kr.gdu.logic.Exchange;

public interface ExchangeMapper {

	@Insert("insert into exchange (code,name,primeamt,sellamt,buyamt,edate) " 
		      + " values (#{code},#{name},#{primeamt},#{sellamt},#{buyamt},#{edate})")
	void insert(Exchange ex);
}
