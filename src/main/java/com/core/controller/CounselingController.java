package com.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/counseling")
public class CounselingController {
	
	@GetMapping("/add")
	public String requestMain(Model model) {
		return "main";
	}
	
}
