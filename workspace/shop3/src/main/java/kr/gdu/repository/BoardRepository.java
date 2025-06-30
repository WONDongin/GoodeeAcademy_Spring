package kr.gdu.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import kr.gdu.domain.Board;
//JpaSpecificationExecutor<Board> : 쿼리를 위한 조건 사용할 수 있는 권한.
@Transactional
public interface BoardRepository extends JpaRepository<Board, Integer>,
                                JpaSpecificationExecutor<Board>{

	@Query("SELECT COALESCE(MAX(b.num), 0) FROM Board b")
	int maxNum();

	@Modifying
	@Query("Update Board b set b.readcnt = b.readcnt+1 where b.num = :num")
	void addReadcnt(@Param("num") Integer num);

	@Modifying
    @Query("UPDATE Board b SET b.grpstep = b.grpstep + 1 WHERE b.grp = :grp AND b.grpstep > :grpstep")	
	void grpStepAdd(@Param("grp") int grp, @Param("grpstep") int grpstep);

	//글쓴이별 작성건수 :JPQL 방식
	@Query("SELECT b.writer AS writer, COUNT(b) AS cnt "
			+ " FROM Board b WHERE b.boardid = :boardid "
			+ " GROUP BY b.writer ORDER BY cnt DESC")
	List<Map<String, Object>> graph1(@Param("boardid") String id);

	//작성일자별 게시물 등록 건수 -> nativequery 필요. db자체의 sql 구문으로 작성
	@Query(value = "SELECT DATE_FORMAT(regdate, '%Y-%m-%d') AS day, "
			+ " COUNT(*) AS cnt "
            + "FROM board WHERE boardid = :boardid "
            + "GROUP BY DATE_FORMAT(regdate, '%Y-%m-%d') "
            + "ORDER BY day DESC LIMIT 7", nativeQuery = true)	
	List<Map<String, Object>> graph2(@Param("boardid") String id);
}
