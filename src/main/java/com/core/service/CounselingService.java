package com.core.service;

import java.util.List;

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
    public Counseling createCounseling(Counseling counseling) {
        return counselingRepository.save(counseling);
    }

    @Transactional
    public Counseling updateCounseling(Long id, Counseling newData) {
        Counseling counseling = findById(id); // 기존 데이터 조회
        counseling.setTitle(newData.getTitle());
        counseling.setContent(newData.getContent());
        counseling.setStatus(newData.getStatus());
        counseling.setDealer(newData.getDealer());
        return counseling;  // JPA dirty checking 자동 반영
    }

    @Transactional
    public void deleteCounseling(Long id) {
    	counselingRepository.deleteById(id);
    }
}