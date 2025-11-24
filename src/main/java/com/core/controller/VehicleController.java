package com.core.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.core.entity.Vehicle;
import com.core.service.VehicleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
@Controller
@RequestMapping("/vehicles")
public class VehicleController {
	
	private final VehicleService vehicleService;
	
	// 페이징 설정
	private int pageSize = 12;
	private int pageBlock = 5;

	
	// 전체 및 검색에 따른 차량 목록 조회 -> 페이징 처리
	@GetMapping
	public String requestVehicleList(@RequestParam(name = "keyword", required=false) String keyword, 
			@PageableDefault(
					page = 0,                      // 처음 접속하면 첫 페이지부터 보여줌
					size = 12,                     // 한 페이지에서 보여주는 개수
					sort = "name",                 // 정렬 기준 필드
					direction = Sort.Direction.ASC // 정렬 방법
			) Pageable pageable, Model model) {
		
	    Page<Vehicle> paging = vehicleService.getVehiclePage(keyword, pageable);
	    log.info("페이지" + paging.toString());
	    

	    model.addAttribute("keyword", keyword);
	    model.addAttribute("paging", paging);                     // 전체 Page 전달
	    model.addAttribute("vehicleList", paging.getContent());   // 본문 카드 목록
		return "vehicle/vehicles";
	}

	
	
	
	
	
	// 차량 상세 정보 조회 - 모델 코드로 조회
	@GetMapping("/vehicle/{modelCode}")
	public String requestVehicleDetail(@PathVariable("modelCode") String modelCode, Model model) {
		Vehicle vehicle = vehicleService.getVehicleByModelCode(modelCode);
		
		model.addAttribute("vehicle", vehicle);
		return "vehicle/vehicle";
	}
	
	// 딜러 차량 등록 폼
	@GetMapping("/dealer/add")
	public String addVehicleForm(Model model) {
		model.addAttribute("vehicle", new Vehicle());
		return "vehicle/addVehicle";
	}
	
	private String fileDir = "c:/5core/";
	
	// 차량 등록 처리
	@PostMapping("/dealer/add")
	public String addVehiclePrdc(@Valid @ModelAttribute Vehicle vehicle, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "vehicle/addVehicle";
		}
		// 업로드 처리
		MultipartFile vehicleImage = vehicle.getVehicleImage();
		String saveName = vehicleImage.getOriginalFilename();
		File saveFile = new File(fileDir, saveName);
		
		if(vehicleImage != null && !vehicleImage.isEmpty()) {
			try {
				vehicleImage.transferTo(saveFile);
			} catch(IOException e) {
				e.printStackTrace();
				throw new RuntimeException("차량 이미지 업로드를 실패하였습니다.");
			}
			vehicle.setFileName(saveName);
		} else {
			vehicle.setFileName("no_image.jpg");
		}
		this.vehicleService.addVehicle(vehicle);
		return "redirect:/vehicles";
	}
}
