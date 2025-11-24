package com.core.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.core.entity.Vehicle;
import com.core.service.VehicleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
@Controller
@RequestMapping("/vehicles")
public class VehicleController {
	
	private final VehicleService vehicleService;
	
	// 전체 및 검색에 따른 차량 목록 조회 -> 페이징 처리
	@GetMapping
	public String requestVehicleList(@RequestParam(name = "keyword", required=false) String keyword, 
			@PageableDefault(
					page = 0,                      // 처음 접속하면 첫 페이지부터 보여줌
					size = 20,                     // 한 페이지에서 보여주는 개수
					sort = "name",                 // 정렬 기준 필드
					direction = Sort.Direction.ASC // 정렬 방법
			) Pageable pageable, Model model) {
		
		Page<Vehicle> vehicles = vehicleService.getVehiclePage(keyword, pageable);
		
		model.addAttribute("keyword", keyword);   // 검색어 유지
		model.addAttribute("vehicles", vehicles);
		return "vehicle/vehicles";
	}
	
	// 차량 상세 정보 조회
	@GetMapping("/vehicle/{modelCode}")
	public String requestVehicleDetail(@PathVariable("modelCode") String modelCode, Model model) {
		Vehicle vehicle = vehicleService.getVehicleByModelCode(modelCode);
		
		model.addAttribute("vehicle", vehicle);
		return "vehicle/vehicle";
	}
}
