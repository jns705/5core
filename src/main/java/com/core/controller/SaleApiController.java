package com.core.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
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
    
    /*
     * 딜러별 sale 판매 원형 그래프
     */
    @GetMapping(value = "/vehicle-sales/me", produces = MediaType.APPLICATION_JSON_VALUE)  // JSON 강제
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
    
    
    
    // 딜러별 월매출 그래프
    @GetMapping(value = "/monthly-sales", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Map<String, Object>> getMonthlySales(
            Principal principal,
            @RequestParam(value = "dealerId", required = false) Long dealerId,
            @RequestParam(value = "memberId", required = false) Long memberId,
            @RequestParam(value = "year", defaultValue = "2025") int year) {

        try {
            // 1) dealerId 직접 들어오면 그걸 우선 사용
            if (dealerId != null) {
                System.out.println("🔍 dealerId 파라미터=" + dealerId);
            } else if (memberId != null) {
                // 2) memberId → Dealer
                System.out.println("🔍 memberId=" + memberId + " → 딜러 조회");
                Member m = memberRepository.findById(memberId).orElse(null);
                if (m != null) {
                    Dealer d = dealerRepository.findByMember(m).orElse(null);
                    if (d != null) dealerId = d.getId();
                }
            } else if (principal != null) {
                // 3) 로그인 사용자 → Dealer
                System.out.println("🔍 principal=" + principal.getName() + " → 딜러 조회");
                Member m = memberRepository.findByMemberId(principal.getName()).orElse(null);
                if (m != null) {
                    Dealer d = dealerRepository.findByMember(m).orElse(null);
                    if (d != null) dealerId = d.getId();
                }
            }

            // 딜러 못 찾으면 0 리턴
            if (dealerId == null) {
                System.out.println("⚠️ 딜러 식별 실패 → 빈 데이터");
                return createEmptyMonthlyData();
            }

            System.out.println("📊 월별실적 조회: dealerId=" + dealerId + ", year=" + year);
            List<Object[]> rows = saleRepository.findMonthlySalesByDealer(year, dealerId);

            String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
            List<Map<String,Object>> result = new ArrayList<>();

            for (String m : months) {
                Map<String,Object> rowMap = new HashMap<>();
                rowMap.put("month", m);
                rowMap.put("salesCount", 0L);

                for (Object[] r : rows) {
                    String dbMonth = r[0].toString().trim();
                    if (dbMonth.equals(m)) {
                        rowMap.put("salesCount", ((Number) r[1]).longValue());
                        break;
                    }
                }
                result.add(rowMap);
            }

            // 성장률
            if (result.size() == 12) {
                long dec = (Long) result.get(11).get("salesCount");
                long nov = (Long) result.get(10).get("salesCount");
                double gr = (nov > 0) ? (dec - nov) * 100.0 / nov : (dec > 0 ? 100.0 : 0.0);
                result.get(11).put("growthRate", Math.round(gr * 10.0) / 10.0);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return createEmptyMonthlyData();
        }
    }

    private List<Map<String, Object>> createEmptyMonthlyData() {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        List<Map<String, Object>> emptyData = new ArrayList<>();
        for (String month : months) {
            Map<String, Object> data = new HashMap<>();
            data.put("month", month);
            data.put("salesCount", 0L);
            emptyData.add(data);
        }
        return emptyData;
    }







    
    
    
}
