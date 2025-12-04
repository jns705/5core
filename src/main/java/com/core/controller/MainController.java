package com.core.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.core.entity.ApplyStatus;
import com.core.entity.Counseling;
import com.core.entity.Dealer;
import com.core.entity.Member;
import com.core.entity.Role;
import com.core.service.CounselingService;
import com.core.service.CustomerService;
import com.core.service.DealerService;
import com.core.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
//@RequestMapping("/main")
public class MainController {


	private final MemberService memberService;
	private final CustomerService customerService;
	private final DealerService dealerService;
	private final CounselingService counselingService;


	@GetMapping("/main")
	public String requestCarList(Model model) {
		return "main";
	}

	// 고객센터 페이지
	@GetMapping("/support")
	public String requestCustomerService() {
		return "customer"; 
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
		Page<Counseling> counselingList = counselingService.findCounselingsByFilter(ApplyStatus.COUNSELING_HODDING.getStatusName(), null, pageable);

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
		Page<Counseling> counselingList = counselingService.findCounselingsByFilter(ApplyStatus.COUNSELING_HODDING.getStatusName(), null, pageable);

		model.addAttribute("counselingList", counselingList);

		return "dealer/careList";
	}

	// 딜러가 상담 선택
	@GetMapping("/care/choice/{id}/{status}/{counselingLikeTime}")
	public String requestUpdateStatus(Principal principal,
			@PathVariable("id") Long id,
			@PathVariable("status") String status,
			@PathVariable("counselingLikeTime") String counselingLikeTime,
			Model model) {

		Member member = memberService.findByMemberId(principal.getName());
		Dealer dealer = dealerService.findByMember(member);

		if(ApplyStatus.COUNSELING_HODDING.getStatusName().equals(status)) {
			return "redirect:/dealer/care";
		}else if(ApplyStatus.COUNSELING_PROGRESS.getStatusName().equals(status)) {
			status = ApplyStatus.COUNSELING_PROGRESS.getStatusName();
		}

		Counseling counseling = counselingService.findById(id);
		counseling.setDealer(dealer);
		counseling.setStatus(status);

		counseling.setCounselingLikeTime(counselingLikeTime);

		counselingService.createCounseling(counseling);

		return "redirect:/dealer/myCustomer";
	}

	// 딜러의 상담고객
	@GetMapping("/dealer/myCustomer")
	public String requestMyCustomer(@RequestParam(value="page", defaultValue="0") int page,
			Principal principal,
			Model model) {

		Sort sortObj = Sort.by("createDate").ascending();
		Pageable pageable = PageRequest.of(page, 10, sortObj);  


		Member member = memberService.findByMemberId(principal.getName());
		Dealer dealer = dealerService.findByMember(member);
		Page<Counseling> counselingList = counselingService.findByDealerId(dealer.getId(), pageable);

		model.addAttribute("counselingList", counselingList);

		return "dealer/myCustomerList";
	}

	// 딜러의 상담고객
	@GetMapping("/dealer/myCustomer/{id}")
	public String requestMyCustomerDetail(@PathVariable("id") Long id,
			Model model) {

		Counseling counseling = counselingService.findById(id);

		model.addAttribute("counseling", counseling);

		return "dealer/myCustomerDetail";
	}

	/*
	 * -------------------------------------------------------------------------------------------
	 * ADMIN
	 * -------------------------------------------------------------------------------------------
	 */
	// 관리자(Role.ADMIN)		
	@GetMapping("/admin")
	public String requestAdminMain() {

		return "redirect:/admin/profile";
	}

	@GetMapping("/admin/profile")
	public String requestAdminProfile(Principal principal,
			@RequestParam(value="page", defaultValue="0") int page,
			Model model) {
		Member admin = memberService.findByMemberId(principal.getName());

		List<Member> customerList = memberService.findByRole(Role.CUSTOMER);

		Sort sortObj = Sort.by("createDate").ascending();
		Pageable pageable = PageRequest.of(page, 10, sortObj);  
		Page<Counseling> counselingList = counselingService.findCounselingsByFilter(ApplyStatus.COUNSELING_HODDING.getStatusName(), null, pageable);

		model.addAttribute("counselingList", counselingList);
		model.addAttribute("customerList", customerList);
		model.addAttribute("admin", admin);

		return "admin/profile";
	}

	@GetMapping("/admin/list")
	public String list(
			Model model,
			@RequestParam(value = "role", defaultValue = "ALL") String role,
			@RequestParam(value = "keyword", defaultValue = "") String keyword,
			// 기본 정렬: PK ID 오름차순
			@PageableDefault(size = 15, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

		Page<Member> memberPage = memberService.getMemberList(pageable, role, keyword);

		// 페이지 블록 계산 (5개 블록)
		int startPage = Math.max(0, memberPage.getNumber() - 2);
		int endPage = Math.min(memberPage.getTotalPages() - 1, memberPage.getNumber() + 2);

		model.addAttribute("memberPage", memberPage);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("roles", Role.values());

		model.addAttribute("currentRole", role);
		model.addAttribute("currentKeyword", keyword);

		Sort.Order order = pageable.getSort().iterator().next();
		String sortProperty = order.getProperty();
		String sortDirection = order.getDirection().toString().toLowerCase(); // 'asc' 또는 'desc'

		model.addAttribute("currentSort", String.format("%s,%s", sortProperty, sortDirection));
		return "admin/list";
	}

	// 관리자 멤버 상태 업데이트
	@PostMapping("/admin/update-status")
	public String updateMemberStatus(@RequestParam("id") Long id, 
			@RequestParam("status") String status,
			RedirectAttributes redirectAttributes) { 
		try {
			memberService.updateMemberStatus(id, status); 

			redirectAttributes.addFlashAttribute("successMessage", 
					String.format("PK ID [%d]의 상태가 '%s'로 성공적으로 수정되었습니다.", id, status));
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		} catch (DataAccessException e) { 
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMessage", "데이터베이스 오류: 상태를 저장하는 데 실패했습니다. (값 길이 초과, 형식 오류 등) 관리자에게 문의하세요.");
		} catch (Exception e) {
			e.printStackTrace(); 
			redirectAttributes.addFlashAttribute("errorMessage", "알 수 없는 오류: 업데이트 중 치명적인 오류가 발생했습니다.");
		}
		return "redirect:/admin/list";
	}
}
