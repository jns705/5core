package com.core.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.core.entity.Counseling;
import com.core.entity.Customer;
import com.core.entity.Dealer;
import com.core.entity.Sale;
import com.core.entity.Vehicle;
import com.core.service.CounselingService;
import com.core.service.CustomerService;
import com.core.service.DealerService;
import com.core.service.MemberService;
import com.core.service.SaleService;
import com.core.service.VehicleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
@Controller
@RequestMapping("/sale")
public class SaleController {

    private final SaleService saleService;
    private final CounselingService counselingService;
    private final MemberService memberService;
    private final DealerService dealerService;
    private final CustomerService customerService;
    private final VehicleService vehicleService;
    

    // 구매 리스트 페이지
    @GetMapping("/list")
    public String saleList(Model model) {
        List<Sale> saleList = saleService.findAll();
        model.addAttribute("saleList", saleList);
        return "member/saleList";
    }
    
    // 딜러 상담 후 구매확정
    @GetMapping("/create/{id}/{price}")
    public String salerCreate(@PathVariable("id") Long id,
    		 @PathVariable("price") int price) {
    	
    	Sale sale = new Sale();
    	
    	Counseling counseling = counselingService.findById(id);
    	counselingService.updateCounselingStatus(id, "구매완료");
    	sale.setCounseling(counseling);
    	
    	Optional<Dealer> dealer = dealerService.findById(counseling.getDealer().getId());
    	sale.setDealer(dealer.get());
    	
    	Optional<Customer> customer = customerService.findById(counseling.getCustomer().getId());
    	int idx = customer.get().getPurchaseCount();
    	customer.get().setPurchaseCount(idx++);
    	customerService.saveCustomer(customer.get(), memberService.findByMemberId(customer.get().getMember().getMemberId()));
    	sale.setCustomer(customer.get());
    	
    	Optional<Vehicle> vehicle = vehicleService.findById(Long.parseLong(counseling.getVehicleId()));
    	sale.setVehicle(vehicle.get());
    	
    	sale.setPrice(price);
    	sale.setSaleDate(LocalDate.now().toString());
    	
    	saleService.saveSale(sale);
    	
    	return "redirect:/dealer";
    }
    
    
    
    
    
    
    
    
    
    
    
    
}
