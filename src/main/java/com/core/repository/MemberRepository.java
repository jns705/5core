package com.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.core.entity.Member;
import com.core.entity.Role;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	
	Optional<Member> findByMemberId(String memberId);
	
	List<Member> findByRole(Role role);
	
	// 회원정보 수정
	public final String UPDATE_MEMBER = "update member m inner join address a on m.address_id = a.id "
			+ " set password = :#{#member.password}, phone = :#{#member.phone}, email = :#{#member.email}, "
			+ " gender = :#{#member.gender}, country = :#{#member.address.country}, zipcode = :#{#member.address.zipcode}, "
			+ " basic_address = :#{#member.address.basicAddress}, detail_address = :#{#member.address.detailAddress} "
			+ " where member_id = :#{#member.memberId}";
	@Transactional
	@Modifying
	@Query(value = UPDATE_MEMBER, nativeQuery = true)
	void queryUpdateMember(@Param("member") Member member);
	
}
