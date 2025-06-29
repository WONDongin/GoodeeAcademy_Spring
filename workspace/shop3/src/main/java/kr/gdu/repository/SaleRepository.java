package kr.gdu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kr.gdu.domain.Sale;

public interface SaleRepository extends JpaRepository<Sale, Integer>{
	@Query("SELECT COALESCE(MAX(s.saleid), 0) FROM Sale s")
	int getMaxSaleId();

	List<Sale> findByUserid(String userid);

}
