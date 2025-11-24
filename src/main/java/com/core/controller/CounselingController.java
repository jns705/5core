package com.core.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.core.entity.Counseling;
import com.core.service.CounselingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/counseling")
public class CounselingController {

    private final CounselingService counselingService;

    // 전체 조회 
    @GetMapping
    public List<Counseling> list() {
        return counselingService.findAll();
    }

    // 상담 글 단건 조회
    @GetMapping("/{id}")
    public Counseling get(@PathVariable Long id) {
        return counselingService.findById(id);
    }

    // 상담 글 등록
    @PostMapping
    public Counseling create(@RequestBody Counseling counseling) {
        return counselingService.createCounseling(counseling);
    }

    // 상담 글 수정
    @PutMapping("/{id}")
    public Counseling update(@PathVariable Long id, @RequestBody Counseling newData) {
        return counselingService.updateCounseling(id, newData);
    }

    // 상담 글 삭제
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        counselingService.deleteCounseling(id);
        return "deleted";
    }
}