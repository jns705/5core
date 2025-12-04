package com.core.controller;

import java.security.Principal;
import java.time.LocalDateTime;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // RedirectAttributes import 추가

import com.core.entity.ApplyStatus;
import com.core.entity.Counseling;
import com.core.entity.Customer;
import com.core.entity.Member;
import com.core.entity.Vehicle; // Vehicle import 추가
import com.core.service.CounselingService;
import com.core.service.CustomerService;
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
	private final VehicleService vehicleService; // VehicleService 의존성 주입
	private final CustomerService customerService;

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
	
	// [수정: 페이징, 필터, 검색 기능 통합]
	@GetMapping("/applyList")
	public String counselingApplyList(
	    @RequestParam(value="page", defaultValue="0") int page,
	    @RequestParam(value="statusFilter", required=false, defaultValue="전체 상태") String status,
	    @RequestParam(value="sortFilter", required=false, defaultValue="등록일 최신순") String sort,
	    @RequestParam(value="keyword", required=false, defaultValue="") String keyword,
	    Model model) {
	    
		// 정렬 기준 설정
		// '예산' 필드가 없으므로 '등록일' 기준으로만 정렬 로직을 작성합니다.
		Sort sortObj;
		if (sort.equals("등록일 오래된순")) {
			sortObj = Sort.by("createDate").ascending();
		} else { // 기본: "등록일 최신순" 또는 그 외
			sortObj = Sort.by("createDate").descending();
		}
		
	    Pageable pageable = PageRequest.of(page, 10, sortObj);  
	    Page<Counseling> counselingPage = counselingService.findCounselingsByFilter(status, keyword, pageable); 

	    model.addAttribute("applyList", counselingPage.getContent());
	    model.addAttribute("counselingPage", counselingPage); // 페이징 정보 전달
	    model.addAttribute("currentPage", page);
	    model.addAttribute("statusFilter", status); // 현재 필터 상태를 뷰에 전달
	    model.addAttribute("sortFilter", sort);     // 현재 정렬 상태를 뷰에 전달
	    model.addAttribute("keyword", keyword);     // 현재 검색 키워드를 뷰에 전달
	    
	    return "counseling/applyList";
	}
	
	/*
	 * 상담신청 등록
	 */
	@PostMapping("/add")
	@PreAuthorize("isAuthenticated()")
	public String addCounselingApply(@Valid @ModelAttribute Counseling counseling, BindingResult bindingResult, Principal principal, Model model) {

		List<Vehicle> getVehicleList = vehicleService.getVehicleList();
		Long vehicleId =  Long.parseLong(counseling.getVehicleId());
		for (Vehicle v: getVehicleList) {
			if (v.getId().equals(vehicleId)) {
				System.out.println(v.getId());
				System.out.println(counseling.getVehicleId());
				counseling.setVehicleName(v.getName());
				break;
			}
		}
				
		if (bindingResult.hasErrors()) {
			model.addAttribute("vehicleList", getVehicleList);			
			return "counseling/addApply";
		}
		
		counseling.setStatus(ApplyStatus.COUNSELING_HODDING.getStatusName()); 
		
		Member member = memberService.findByMemberId(principal.getName());
		Customer customer = customerService.findByMember(member);
		
		counseling.setCustomer(customer);
		counseling.setCreateDate(LocalDateTime.now());
		
		List<Counseling> getCounselingList =  counselingService.findByCustomerId(member.getId());
				
		for (Counseling  getCounseling : getCounselingList) {
			if (getCounseling.getVehicleId().equals(counseling.getVehicleId())) {
				bindingResult.rejectValue("vehicleId", "duplecatedVehicleId", "이미 존재하는 상품입니다.");
				model.addAttribute("vehicleList", getVehicleList);
				return "counseling/addApply";
			}
		}
					
		counselingService.createCounseling(counseling);	
		
		return "redirect:/counseling/applyList";
	}
	
	// 상세 조회 -> 상세/수정 화면
	@GetMapping("/detail/{id}")
	public String counselingApplyDetail(@PathVariable("id") Long id, Model model) {
	    Optional<Counseling> counselingOpt = counselingService.getCounselingById(id);
	    
	    if (counselingOpt.isPresent()) {
		    // 수정 화면에 필요한 차량 목록 등을 추가합니다.
		    	List<Vehicle> getVehicleList = vehicleService.getVehicleList();
		    	model.addAttribute("vehicleList", getVehicleList);
	        model.addAttribute("counseling", counselingOpt.get());
	        return "counseling/applyDetail"; 
	    } else {
	        return "redirect:/counseling/applyList";
	    }
	}

	/*
	 * 상담 수정 처리
	 */
	@PostMapping("/update/{id}")
	public String updateCounseling(
	    @PathVariable("id") Long id, 
	    @Valid @ModelAttribute Counseling counseling, 
	    BindingResult bindingResult,
	    RedirectAttributes redirectAttributes,
	    Model model) {

		// 유효성 검사
		if (bindingResult.hasErrors()) {
			List<Vehicle> getVehicleList = vehicleService.getVehicleList();
			model.addAttribute("vehicleList", getVehicleList);
			return "counseling/applyDetail"; 
		}

		try {
		    counselingService.updateCounseling(id, counseling);
		    redirectAttributes.addFlashAttribute("message", "상담 내용이 성공적으로 수정되었습니다.");
		    
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("error", "상담 ID를 찾을 수 없습니다.");
		    return "redirect:/counseling/applyList";
		}
	    return "redirect:/counseling/detail/" + id; 
	}
	
	@GetMapping("/applyList/cencel/{id}")
	public String updateCounselingStatus(@PathVariable("id") Long id, 
		    RedirectAttributes redirectAttributes,
		    Model model) {
		
		try {
		    counselingService.updateCounselingStatus(id, ApplyStatus.COUNSELING_CENCEL.getStatusName());
		    redirectAttributes.addFlashAttribute("message", "상담신청이 취소되었습니다.");
		    
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("error", "상담 ID를 찾을 수 없습니다.");
		    return "redirect:/counseling/applyList";
		}

		return counselingApplyList(0, null, "전체상태", null, model); 
	}
	
	/*
	 * 상담 상태만 수정
	 */
	@GetMapping("/update/{id}/{status}")
	public String updateStatus(@PathVariable("id") Long id, 
			@PathVariable("status") String status) {
		
		Counseling counseling = counselingService.findById(id);
		counseling.setStatus(status);
		counselingService.createCounseling(counseling);
		
		return "redirect:/dealer/myCustomer";
	}
	
}