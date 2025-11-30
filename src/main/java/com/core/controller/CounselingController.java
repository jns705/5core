package com.core.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.core.entity.ApplyStatus;
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
	
	// 상담신청 리스트 (페이징)
	@GetMapping("/applyList")
	public String counselingApplyList(
	    @RequestParam(value="page", defaultValue="0") int page,
	    Model model) {
	    
	    Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
	    Page<Counseling> counselingPage = counselingService.findAll(pageable); 

	    model.addAttribute("applyList", counselingPage.getContent());
	    model.addAttribute("counselingPage", counselingPage); // 페이징 정보 전달
	    model.addAttribute("currentPage", page);
	    return "counseling/applyList";
	}
	
	/*
	 * 상담신청 등록
	 */
	@PostMapping("/add")
	@PreAuthorize("isAuthenticated()")
	public String addCounselingApply(@Valid @ModelAttribute Counseling counseling, BindingResult bindingResult, Principal principal, Model model) {
		
		List<Vehicle> getVehicleList = vehicleService.getVehicleList();
				
		// 등록확인을 하려면 주석필요
		if (bindingResult.hasErrors()) {
			model.addAttribute("vehicleList", getVehicleList);			
			return "counseling/addApply";
		}
		
		// 상담 상태: 상담대기중
		counseling.setStatus(ApplyStatus.COUNSELING_HODDING.getStatusName());
		
		Member member = memberService.findByMemberId(principal.getName());
		Customer customer = new Customer();
		customer.setId(member.getId());
		customer.setMember(member);
		counseling.setCustomer(customer);
				
		// 차량 이름 검색
		Long csVehicleId = Long.parseLong(counseling.getVehicleId());
		Optional<Vehicle> getVehicle = vehicleService.getVehicleByVehicleId(csVehicleId);
		
		counseling.setVehicleId(getVehicle.get().getName());
				
		counselingService.createCounseling(counseling);	
		return "redirect:/counseling/applyList";
	}
	
	// 상담 글 상세 조회
	@GetMapping("/detail/{id}")
	public String counselingApplyDetail(@PathVariable("id") Long id, Model model) {
	    Optional<Counseling> counselingOpt = counselingService.getCounselingById(id);
	    
	    if (counselingOpt.isPresent()) {
	        model.addAttribute("counseling", counselingOpt.get());
	        return "counseling/applyDetail";
	    } else {
	        // ID에 해당하는 상담이 없을 경우 에러 페이지 또는 리스트로 리다이렉트
	        return "redirect:/counseling/applyList";
	    }
	}
}
