package com.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/counseling")
public class CounselingController {

    // 상담 글 단건 조회
    @GetMapping
    public String counseling() {
        return "counseling/counseling";
    }

	@GetMapping("/detail")
	public String counselingDetail() {
	    return "counseling/counselingDetail";
}
}
    