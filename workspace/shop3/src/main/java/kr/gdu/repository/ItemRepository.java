package kr.gdu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kr.gdu.domain.Item;
//JpaRepository<클래스,기본키자료형> 의 하위 인터페이스는 자동 객체 생성함.
@Repository
public interface ItemRepository extends JpaRepository<Item,Integer>{
//	List<Item> findAll(); //전체 데이터 조회

	@Query("select coalesce(max(i.id),0) from Item i")
	int findMaxId();
}
