package com.core.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.core.entity.Member;
import com.core.entity.Role;
import com.core.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
//@RequestMapping("/main")
public class MainController {
	
	private final MemberService memberService;

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
	public String requestDealerProfile(Principal principal, Model model) {
		Member dealer = memberService.findByMemberId(principal.getName());
		
		List<Member> customerList = memberService.findByRole(Role.CUSTOMER);
		
		model.addAttribute("customerList", customerList);
		model.addAttribute("dealer", dealer);
		
		return "dealer/profile";
	}
	
	// 딜러
	@GetMapping("/dealer/care")
	public String requestDealercarList() {
		
		return "dealer/careList";
	}
	
	
	
	
}
