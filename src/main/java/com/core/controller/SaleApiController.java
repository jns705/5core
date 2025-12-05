package com.core.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.entity.Dealer;
import com.core.entity.Member;
import com.core.entity.Sale;
import com.core.repository.DealerRepository;
import com.core.repository.MemberRepository;
import com.core.repository.SaleRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sales")
public class SaleApiController {
    
    private final SaleRepository saleRepository;
    private final MemberRepository memberRepository;
    private final DealerRepository dealerRepository;
    
    @GetMapping(value = "/vehicle-sales/me", produces = MediaType.APPLICATION_JSON_VALUE)  // ✅ JSON 강제
    public List<Map<String, Object>> getMyVehicleSales(
            Principal principal,
            @RequestParam(value = "memberId", required = false) Long memberId) {
        
        Member member = null;
        if (memberId != null) {
            Optional<Member> optMember = memberRepository.findById(memberId);
            if (optMember.isPresent()) member = optMember.get();
        }
        
        if (member == null && principal != null) {
            Optional<Member> optMember = memberRepository.findByMemberId(principal.getName());
            if (optMember.isPresent()) member = optMember.get();
        }
        
        if (member == null) {
            throw new IllegalArgumentException("memberId 또는 로그인 필요");
        }
        
        Optional<Dealer> optDealer = dealerRepository.findByMember(member);
        if (optDealer.isEmpty()) {
            throw new IllegalArgumentException("딜러 없음: " + member.getMemberId());
        }
        Dealer dealer = optDealer.get();
        
        List<Sale> sales = saleRepository.findByDealer(dealer);
        
        Map<Long, Map<String, Object>> vehicleStats = new HashMap<>();
        for (Sale sale : sales) {
            Long vid = sale.getVehicle().getId();
            String vehicleName = sale.getVehicle().getName() != null 
                ? sale.getVehicle().getName() 
                : "차량_" + vid;
                
            if (!vehicleStats.containsKey(vid)) {
                Map<String, Object> stat = new HashMap<>();
                stat.put("vehicleId", vid);
                stat.put("vehicleName", vehicleName);
                stat.put("salesCount", 0L);
                stat.put("totalPrice", 0L);
                vehicleStats.put(vid, stat);
            }
            
            Map<String, Object> stat = vehicleStats.get(vid);
            stat.put("salesCount", (Long)stat.get("salesCount") + 1);
            stat.put("totalPrice", (Long)stat.get("totalPrice") + (sale.getPrice() != 0 ? sale.getPrice() : 0));
        }
        
        return new ArrayList<>(vehicleStats.values());
    }
}
