package com.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.core.entity.Sale;
import com.core.repository.CounselingRepository;
import com.core.repository.SaleRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SaleService {

    private final SaleRepository saleRepository;

    public List<Sale> findAll() {
        return saleRepository.findAll();
    }

    public Sale findById(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 판매 ID: " + id));
    }

    @Transactional
    public Sale saveSale(Sale sale) {
        return saleRepository.save(sale);
    }
}
