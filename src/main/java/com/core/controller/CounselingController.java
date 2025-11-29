package com.core.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.core.entity.Counseling;
import com.core.entity.Customer;
import com.core.entity.Member;
import com.core.entity.Vehicle;
import com.core.service.CounselingService;
import com.core.service.MemberService;
import com.core.service.VehicleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
@Controller
@RequestMapping("/counseling")
public class CounselingController {
	
	private final CounselingService counselingService;
	private final MemberService memberService;
	private final VehicleService vehicleService;

	// 상담 글 단건 조회
	@GetMapping
	public String counselingApply() {
		return "counseling/apply";
	}

	@GetMapping("/addApply")
	public String counselingApplyAdd(Model model) {
			
		List<Vehicle> getVehicleList = vehicleService.getVehicleList();
		
		model.addAttribute("vehicleList", getVehicleList);
		model.addAttribute("counseling", new Counseling());
		return "counseling/addApply";
	}
	
	@GetMapping("/applyList")
	public String counselingApplyList(Model model) {
		List<Counseling> applyList = counselingService.findAll();
		model.addAttribute("applyList", applyList);
		return "counseling/applyList";
	}
	
	/*
	 * 상담신청 등록
	 */
	@PostMapping("/add")
	@PreAuthorize("isAuthenticated()")
	public String addCounselingApply(@Valid @ModelAttribute Counseling counseling, BindingResult bindingResult, Principal principal, Model model) {
		
		// 등록확인을 하려면 주석필요
		if (bindingResult.hasErrors()) {
			return "counseling/addApply";
		}
		
		counseling.setStatus("상담대기");
		  //principal.getName()
		
		Member member = memberService.findByMemberId(principal.getName());
		Customer customer = new Customer();
		customer.setId(member.getId());
		counseling.setCustomer(customer);
		
		Long csVehicleId = Long.parseLong(counseling.getVehicleId());
		// 차량 이름 검색
		Optional<Vehicle> getVehicle = vehicleService.getVehicleByVehicleId(csVehicleId);
		counseling.setVehicleId(getVehicle.get().getName());
		counselingService.createCounseling(counseling);
		
		return "redirect:/counseling/applyList";
	}
	
}
