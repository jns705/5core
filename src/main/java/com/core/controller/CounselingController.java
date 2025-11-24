package com.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class CounselingController {
	
	@GetMapping("/counseling")
	public String requestCounseling(Model model) {
		return "counseling/counseling";
		
	}
}
