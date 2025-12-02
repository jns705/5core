package com.core.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	/**
     * 멤버 리스트 조회 (페이징, 필터링, 검색)
     */
    public Page<Member> getMemberList(Pageable pageable, String role, String keyword) {
        Role filterRole = "ALL".equals(role) ? null : Role.valueOf(role);
        String searchKeyword = (keyword == null || keyword.trim().isEmpty()) ? null : keyword.trim();
        
        return memberRepository.searchAll(filterRole, searchKeyword, pageable);
    }

    /**
     * 멤버 정보 수정 (권한/상태)
     */
    @Transactional
    public Member updateMemberRoleAndStatus(Long id, String newRole, String newStatus) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("멤버 ID: " + id + "를 찾을 수 없습니다."));

        // 권한 업데이트
        try {
            member.setRole(Role.valueOf(newRole));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 역할(Role)입니다.");
        }

        
        // save()는 Transaction 종료 시 자동 호출되나, 명시적으로 호출 가능
        return member;
    }
	

    /**
     * 멤버 정보 수정 (권한만 수정)
     */
    @Transactional
    // ID 타입을 Long으로 변경하고, Member.java에 status가 없어 해당 파라미터는 무시합니다.
    public Member updateMemberRole(Long id, String newRole) { 
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("멤버 PK ID: " + id + "를 찾을 수 없습니다."));

        // 권한 업데이트
        try {
            member.setRole(Role.valueOf(newRole));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 역할(Role)입니다.");
        }
        
        // 상태(status) 필드는 Member 엔티티에 없어 수정 로직을 제외합니다.

        return member;
    }
		
}
