package kr.gdu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.gdu.domain.SaleItem;
import kr.gdu.domain.SaleItemId;

public interface SaleItemRepository 
                 extends JpaRepository<SaleItem, SaleItemId>{

	List<SaleItem> findBySaleid(int saleid);

}
