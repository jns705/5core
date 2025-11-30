package com.core.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.core.entity.Counseling;

@Repository
public interface CounselingRepository extends JpaRepository<Counseling, Long> {

	// 상태별 필터링을 위한 메서드 추가
	Page<Counseling> findByStatus(String status, Pageable pageable);

	// 고객 ID나 연락처를 포함하는 검색을 위한 메서드 추가 (JPA Query Method)
	// Customer 엔티티를 조인하여 Member의 id 또는 phone을 검색
	Page<Counseling> findByCustomer_Member_IdContainingOrCustomer_Member_PhoneContaining(String customerIdKeyword, String phoneKeyword, Pageable pageable);

	// 상태 필터와 검색어를 동시에 적용
	Page<Counseling> findByStatusAndCustomer_Member_IdContainingOrStatusAndCustomer_Member_PhoneContaining(
			String status1, String customerIdKeyword, 
			String status2, String phoneKeyword, 
			Pageable pageable);
	
	List<Counseling> findByCustomerId(Long id);
}

