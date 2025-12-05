package com.core.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.core.entity.Dealer;
import com.core.entity.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
	
	List<Sale> findAll();
	
	List<Sale> findByDealer(Dealer dealer);
	// dealer엔터티에 해당하는 sale 찾기
	Page<Sale> findByDealer(Pageable pageable, Dealer dealer);
	
	
}
