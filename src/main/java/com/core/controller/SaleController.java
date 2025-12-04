package com.core.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.core.entity.Sale;
import com.core.service.SaleService;

@Controller
@RequestMapping("/sale")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    // 구매 리스트 페이지
    @GetMapping("/list")
    public String saleList(Model model) {
        List<Sale> saleList = saleService.findAll();
        model.addAttribute("saleList", saleList);
        return "member/saleList"; 
    }
}
