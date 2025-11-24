package com.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class CounselingController {

    // 상담 글 단건 조회
    @GetMapping("/counseling")
    public String counseling() {
        return "counseling/counseling";
    }
}
    