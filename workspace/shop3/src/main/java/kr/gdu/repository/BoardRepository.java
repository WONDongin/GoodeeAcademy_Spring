package kr.gdu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import kr.gdu.domain.Board;
//JpaSpecificationExecutor<Board> : 쿼리를 위한 조건 사용할 수 있는 권한.
public interface BoardRepository extends JpaRepository<Board, Integer>,
                                JpaSpecificationExecutor<Board>{

	@Query("SELECT COALESCE(MAX(b.num), 0) FROM Board b")
	int maxNum();

	@Transactional
	@Modifying
	@Query("Update Board b set b.readcnt = b.readcnt+1 where b.num = :num")
	void addReadcnt(@Param("num") Integer num);

}
