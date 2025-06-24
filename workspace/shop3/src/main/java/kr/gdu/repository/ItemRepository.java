package kr.gdu.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import kr.gdu.logic.Item;

// JpaRepository<클래스, 기본키자료형>의 하위 인터페이스는 자동 객체 생성함
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer>{
	// List<Item> findAll(); // 전체 데이터 조회
	
	@Query("select coalesce(max(i.id),0) from Item i")
	int findMaxId();
}
