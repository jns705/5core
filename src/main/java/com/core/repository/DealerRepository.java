package com.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.core.entity.Dealer;
import com.core.entity.Member;

@Repository
public interface DealerRepository extends JpaRepository<Dealer, Long>{
	
	Optional<Dealer> findByMember(Member member);
	
}
