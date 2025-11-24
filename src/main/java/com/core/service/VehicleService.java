package com.core.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.core.entity.Vehicle;
import com.core.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class VehicleService {
	
	private final VehicleRepository vehicleRepository;
	
	Vehicle vehicle = new Vehicle();
	
	//############################################################
	// 트림에 따른 가격 계산
	public int getPriceByTrim(Long id) {
		// 차량의 트림 정보를 조회
		vehicle = vehicleRepository.findById(id).get();
		String trim = vehicle.getTrim();
		
		// 트림이 null인 경우 기본 가격
		if(trim == null) return 0;
		
		// switch expression을 사용하여 트림에 따른 가격 리턴
		return switch(trim.toUpperCase()) {
			case "EXCLUSIVE" -> 2000000;
			case "PRESTIGE" -> 4000000;
			default -> 0; // Standard 트림은 기본 가격과 동일
		};
	}
	// 총액 계산 (기본가격 + 트림가격)
	public int getTotalPrice(Long id) {
		vehicle = vehicleRepository.findById(id).get();
		int basePrice = vehicle.getPrice(); // 기본 가격
		int trimPrice = getPriceByTrim(id); // 트림 가격
		
		return basePrice + trimPrice; 		// 총액
	}
	//############################################################
	
	// 전체 상품 목록 조회 -> 페이징 처리 x
	public List<Vehicle> getVehicleList() {
		return vehicleRepository.findAll();
	}
	
	public Page<Vehicle> getVehiclePage(String keyword, Pageable pageable) {
		// 검색어가 없을 경우 전체 목록 조회
		if(keyword == null || keyword.isBlank()) {
			return vehicleRepository.findAll(pageable);
		}
		
		// 검색어가 있는 경우에는 검색
		return vehicleRepository.findByNameContainingOrVehicleTypeContaining(keyword, keyword, pageable);
	}
	
	/*
	public Page<Vehicle> getList(int pageNum, String state, String keyword, int pageSize, String sortField, String sortWay) {
		Pageable pageable = PageRequest.of(pageNum-1, pageSize, 
					sortWay.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
		
		switch(state) {
		case "vehicleType": 
			return vehicleRepository.findByVehicleType(keyword, pageable);
		case "fuelType": 
			return vehicleRepository.findByFuelType(keyword, pageable);
		case "brand": 
			return vehicleRepository.findByBrand(keyword, pageable);
		case "search":  // search일 때 처리(검색)
			return vehicleRepository.findByNameContainingOrVehicleTypeContaining(keyword, keyword, pageable);
		default:        // all일 때 처리
			return vehicleRepository.findAll(pageable);
		}
	}
	*/
	
	// 차량 상세 정보 조회
	public Vehicle getVehicleByModelCode(String modelCode) {
		return vehicleRepository.findByModelCode(modelCode).get();
	}
	
	// 차량 등록
	public void addVehicle(Vehicle vehicle) {
		vehicleRepository.save(vehicle);
	}
}
