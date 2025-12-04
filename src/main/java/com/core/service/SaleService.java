package com.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.core.entity.Sale;
import com.core.repository.SaleRepository;

@Service
@Transactional(readOnly = true)
public class SaleService {

    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public List<Sale> findAll() {
        return saleRepository.findAll();
    }

    public Sale findById(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 판매 ID: " + id));
    }

    @Transactional
    public Sale save(Sale sale) {
        return saleRepository.save(sale);
    }
}
