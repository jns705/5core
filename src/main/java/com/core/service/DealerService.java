package com.core.service;

import org.springframework.stereotype.Service;

import com.core.entity.Dealer;
import com.core.entity.Member;
import com.core.repository.DealerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DealerService {
	
	private final DealerRepository dealerRepository;
	
	public void saveDealer(Dealer dealer, Member member) {
		dealer.setMember(member);
		dealerRepository.save(dealer);
	}
	
	public Dealer findByMember(Member member) {
		return dealerRepository.findByMember(member).get();
	}
}
