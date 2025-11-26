package com.core.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.core.entity.Counseling;
import com.core.service.CounselingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/counseling")
public class CounselingController {
	
	private final CounselingService counselingService;

	// 상담 글 단건 조회
	@GetMapping
	public String counselingApply() {
		return "counseling/apply";
	}

	@GetMapping("/addApply")
	public String counselingApplyAdd(Model model) {
		model.addAttribute("counseling", new Counseling());
		return "counseling/addApply";
	}
	
	@GetMapping("/applyList")
	public String counselingApplyList() {
		return "counseling/applyList";
	}
	
	/*
	 * 상담신청 등록
	 */
	@PostMapping("/add")
	public String addCounselingApply(@Valid @ModelAttribute Counseling counseling, BindingResult bindingResult, Model model) {
		
		if (bindingResult.hasErrors()) {
			return "counseling/addApply";
		}
		
		counselingService.createCounseling(counseling);
		List<Counseling> applyList = counselingService.findAll();
		
		model.addAttribute("applyList", applyList);
		return "counseling/applyList";
	}
	
}
