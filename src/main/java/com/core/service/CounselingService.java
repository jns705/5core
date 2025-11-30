package com.core.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.core.entity.Counseling;
import com.core.repository.CounselingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly= true)
public class CounselingService {

	private final CounselingRepository counselingRepository;

	// 전체 조회
	public List<Counseling> findAll() {
		return counselingRepository.findAll();
	}

	// 상담 단건 조회
	public Counseling findById(Long id) {
		return counselingRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상담입니다."));
	}


	@Transactional
	public void deleteCounseling(Long id) {
		counselingRepository.deleteById(id);
	}
	/**
	 * 전체 상담 목록 조회 (페이징 지원)
	 */
	public Page<Counseling> findAll(Pageable pageable) {
		return counselingRepository.findAll(pageable);
	}
	
	/**
	 * 상담 단건 조회 (Optional 반환)
	 * Controller에서 존재 여부를 확인하기 위해 Optional로 반환합니다.
	 */
	public Optional<Counseling> getCounselingById(Long id) {
		return counselingRepository.findById(id);
	}
	

	/**
	 * 상태 및 검색어 기반으로 상담 목록 조회 (페이징 지원)
	 * @param status 상담 상태 (전체일 경우 null 또는 빈 문자열)
	 * @param keyword 검색어 (고객 ID 또는 연락처)
	 * @param pageable 페이징 정보
	 * @return 조건에 맞는 상담 목록 Page
	 */
	public Page<Counseling> findCounselingsByFilter(String status, String keyword, Pageable pageable) {
	    // 1. 상태 필터만 적용 (검색어 없음)
	    if ((keyword == null || keyword.trim().isEmpty()) && (status != null && !status.equals("전체 상태"))) {
	        return counselingRepository.findByStatus(status, pageable);
	    }
	    
	    // 2. 검색어만 적용 (전체 상태)
	    if (status == null || status.equals("전체 상태")) {
	        if (keyword != null && !keyword.trim().isEmpty()) {
	            String searchKeyword = keyword.trim();
	            return counselingRepository.findByCustomer_Member_IdContainingOrCustomer_Member_PhoneContaining(
	                    searchKeyword, searchKeyword, pageable);
	        }
	        // 3. 필터/검색어 모두 없음 (전체 조회)
	        return counselingRepository.findAll(pageable);
	    }
	    
	    // 4. 상태 필터와 검색어 모두 적용
	    if (status != null && !status.equals("전체 상태") && keyword != null && !keyword.trim().isEmpty()) {
	        String searchKeyword = keyword.trim();
	        return counselingRepository.findByStatusAndCustomer_Member_IdContainingOrStatusAndCustomer_Member_PhoneContaining(
	                status, searchKeyword, 
	                status, searchKeyword, 
	                pageable);
	    }
	    
	    // 5. 기본: 전체 조회
	    return counselingRepository.findAll(pageable);
	}
	
	
	@Transactional
	public Counseling createCounseling(Counseling counseling) {
		return counselingRepository.save(counseling);
	}

	@Transactional
	public Counseling updateCounseling(Long id, Counseling newData) {
		Counseling counseling = findById(id); // 기존 데이터 조회
		// 상태만 업데이트가 아니라, 필요한 필드만 선택적으로 업데이트 해야 합니다.
		if(newData.getStatus() != null) {
		    counseling.setStatus(newData.getStatus());
		}
		if(newData.getDealer() != null) {
		    counseling.setDealer(newData.getDealer());
		}
		// ... 다른 필드 업데이트 로직 추가 가능 ...
		return counseling;  // JPA dirty checking 자동 반영
	}
	
	/**
	 * 상담 상태 변경
	 */
	@Transactional
	public void updateStatus(Long id, String newStatus) {
	    Counseling counseling = findById(id);
	    counseling.setStatus(newStatus);
	}


}