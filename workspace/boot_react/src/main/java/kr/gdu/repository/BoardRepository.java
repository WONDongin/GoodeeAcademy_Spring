package kr.gdu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import kr.gdu.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity,Integer>,
                    JpaSpecificationExecutor<BoardEntity>{

	@Transactional
	@Modifying
	@Query("Update BoardEntity b set b.readcnt = b.readcnt+1"
			+ " where b.num = :num" )
	void addReadcnt(@Param("num") int num);
	
}