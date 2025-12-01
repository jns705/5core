package com.core.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.core.entity.Counseling;
import com.core.entity.Member;
import com.core.entity.Role;
import com.core.service.CounselingService;
import com.core.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
//@RequestMapping("/main")
public class MainController {
	
	private final MemberService memberService;
	private final CounselingService counselingService;

	@GetMapping("/main")
	public String requestCarList(Model model) {
		return "main";
	}
	
	
	
	/*
	 * -------------------------------------------------------------------------------------------
	 * DEALER
	 * -------------------------------------------------------------------------------------------
	 */
	// 딜러(Role.DEALER)
	@GetMapping("/dealer")
	public String requestDealerMain() {
		
		return "redirect:/dealer/profile";
	}
	
	@GetMapping("/dealer/profile")
	public String requestDealerProfile(Principal principal,
			@RequestParam(value="page", defaultValue="0") int page,
			Model model) {
		Member dealer = memberService.findByMemberId(principal.getName());
		
		List<Member> customerList = memberService.findByRole(Role.CUSTOMER);
		
		Sort sortObj = Sort.by("createDate").ascending();
		Pageable pageable = PageRequest.of(page, 10, sortObj);  
		Page<Counseling> counselingList = counselingService.findByStatus(pageable, "상담대기");
		
		model.addAttribute("counselingList", counselingList);
		model.addAttribute("customerList", customerList);
		model.addAttribute("dealer", dealer);
		
		return "dealer/profile";
	}
	
	// 딜러
	@GetMapping("/dealer/care")
	public String requestDealercareList(@RequestParam(value="page", defaultValue="0") int page,
			Model model) {
		Sort sortObj = Sort.by("createDate").ascending();
		Pageable pageable = PageRequest.of(page, 10, sortObj);  
		Page<Counseling> counselingList = counselingService.findByStatus(pageable, "상담대기");
		
		model.addAttribute("counselingList", counselingList);
		
		return "dealer/careList";
	}
	
	// 딜러가 상담 선택
	@GetMapping("/care/choice/{id}/{status}")
	public String requestUpdateStatus(Model model) {
		
		
		return "redirect:/dealer/profile";
	}
	
	
	
	
	
	
	
}
