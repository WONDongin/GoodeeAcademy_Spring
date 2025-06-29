package kr.gdu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.gdu.domain.Comment;
import kr.gdu.domain.CommentId;

public interface CommRepository extends JpaRepository<Comment, CommentId>{

}
