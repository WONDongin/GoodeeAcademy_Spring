package kr.gdu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.gdu.domain.Comment;
import kr.gdu.domain.CommentId;

public interface CommRepository extends JpaRepository<Comment, CommentId>{

	List<Comment> findByNum(Integer num); //JPA에서 자동으로 SQL구문 생성

	@Query("select COALESCE(max(c.seq),0) from Comment c where c.num = :num")
	int maxseq(@Param("num") int num);

}
