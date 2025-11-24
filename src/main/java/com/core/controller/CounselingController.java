package com.core.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.core.service.CounselingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/counseling")
public class CounselingController {

    // 상담 글 단건 조회
    @GetMapping("/main")
    public String counseling() {
        return "counseling";
    }
}
    