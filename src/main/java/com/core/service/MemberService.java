package com.core.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.core.entity.Member;
import com.core.entity.Role;
import com.core.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {
	
	private final MemberRepository memberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
		Optional<Member> _member = memberRepository.findByMemberId(memberId);
		
		if(_member.isEmpty()) {
			throw new UsernameNotFoundException("회원 ID를 찾을 수 없습니다.");
		}
		
		Member member = _member.get();
		
		// 로그인을 할 때 전달받은 회원 ID를 사용하여 비밀번호가 일치하는지를 검사하는 User 객체를 러턴함.
		return User.builder().username(member.getMemberId()).password(member.getPassword())
				.roles(member.getRole().toString()).build();
	}
	
	// 회원가입
	public void saveMember(Member member) {
		memberRepository.save(member);
		
	}
	
	// 회원ID로 회원조회
	public Member findByMemberId(String memberId) {
		return memberRepository.findByMemberId(memberId).get();
	}
	
	// 회원 수정
	public Member updateMember(Member member) {
		memberRepository.queryUpdateMember(member);
		return memberRepository.findByMemberId(member.getMemberId()).get();
	}
	
	// 회원 탈퇴
	public void deleteMember(String memberId) {
		Optional<Member> member = memberRepository.findByMemberId(memberId);
		memberRepository.delete(member.get());
	}
	
	// Role로 회원 조회
	public List<Member> findByRole(Role role) {
		return memberRepository.findByRole(role);
	}
	
	
}
