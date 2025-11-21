package com.core.service;

import org.springframework.stereotype.Service;

import com.core.entity.Customer;
import com.core.entity.Member;
import com.core.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomerService {
	
	private final CustomerRepository customerRepository;
	
	public void saveCustomer(Customer customer, Member member) {
		customer.setMember(member);
		customerRepository.save(customer);
	}
	
}
