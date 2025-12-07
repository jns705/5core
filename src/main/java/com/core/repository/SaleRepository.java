package com.core.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.core.entity.Dealer;
import com.core.entity.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
	
	List<Sale> findAll();
	
	List<Sale> findByDealer(Dealer dealer);
	// dealer엔터티에 해당하는 sale 찾기
	Page<Sale> findByDealer(Pageable pageable, Dealer dealer);
	
	
	// 딜러별 월판매 그래프
	@Query(value = """
		    SELECT DATE_FORMAT(s.sale_date, '%b') AS month_name,
		           COUNT(s.id) AS sales_count
		    FROM sale s
		    WHERE YEAR(s.sale_date) = ?1
		      AND s.dealer_id = ?2
		    GROUP BY MONTH(s.sale_date)
		    ORDER BY MONTH(s.sale_date)
		    """, nativeQuery = true)
		List<Object[]> findMonthlySalesByDealer(int year, Long dealerId);

	
}
