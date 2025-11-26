package com.core.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

	
	// 메인 화면 페이징 처리
	@GetMapping
	public String requestVehicleList(
	        @RequestParam(name = "keyword", required = false) String keyword,
	        @RequestParam(name = "type", required = false) String type,
	        @RequestParam(name = "fuel", required = false) String fuel,
	        @PageableDefault(
	                page = 0,
	                size = 8,
	                sort = "name",
	                direction = Sort.Direction.ASC
	        ) Pageable pageable,
	        Model model) {
		// ALL 버튼용 처리
		// - 조건이 없을 때 null로 처리하여 서비스로 전달 (전체 조회)
		if(keyword != null && keyword.isBlank()) keyword = null;
		if(type != null && type.isBlank()) type = null;
		if(fuel != null && fuel.isBlank()) fuel = null;
		

	    Page<Vehicle> paging = vehicleService.getVehiclePage(keyword, type, fuel, pageable);

	    model.addAttribute("keyword", keyword);
	    model.addAttribute("type", type);
	    model.addAttribute("fuel", fuel);
	    model.addAttribute("paging", paging);
	    model.addAttribute("vehicleList", paging.getContent());
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
