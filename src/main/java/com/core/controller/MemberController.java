package com.core.controller;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.core.entity.Member;
import com.core.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {
	
	private final MemberService memberService;
	
	private final PasswordEncoder passwordEncoder;
	
	// [1] 일반 사용자
	// 회원가입 폼
	@GetMapping("/add")
	public String addMemberForm(Model model) {
		
		model.addAttribute("member", new Member());
		return "member/addMember";
	}
	
	// 회원가입 처리
	@PostMapping("/add")
	public String addMemberPro(@Valid @ModelAttribute Member member, BindingResult bindingResult, Model model) {
		
		boolean roleCheck = false;
		
		// 유효성 검사
		if(bindingResult.hasErrors()) {
			return "member/addMember";
		}
		
		// 입력한 비밀번호 2개가 일치하는지를 검사
		if(!member.getPassword().equals(member.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordIncorrect", "입력한 비밀번호가 일치하지 않습니다.");
			return "member/addMember";
		}
		
		// 중복 ID 체크
		// DataIntegrityViolationException -> unique 속성을 위배했을 때 발생하는 예외
		try {
			////////////////////////////////////////////////////////////////////////////////////////////
			// 딜러, 고객 구분해서 true false 넣어야 함
			////////////////////////////////////////////////////////////////////////////////////////////
			Member m = Member.createMember(member, passwordEncoder, true);
			m.setJoinDate(LocalDateTime.now());
			memberService.saveMember(m);
			
		} catch(DataIntegrityViolationException e) {
			// rejectValue(필드명, 오류코드, 메시지)
			bindingResult.rejectValue("memberId", "duplecatedMemberId", "이미 존재하는 회원 ID입니다.");
			return "member/addMember";
		} catch(Exception e) {
			bindingResult.rejectValue("memberId", "duplecatedMemberId", e.getMessage());
			return "member/addMember";
		}

		return "redirect:/main";
	}
}
