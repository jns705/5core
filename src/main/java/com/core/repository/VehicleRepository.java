package com.core.repository;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.core.entity.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	Optional<Vehicle> findById(Long id);
	
	// 모델 코드로 검색
	Optional<Vehicle> findByModelCode(String modelCode);
	
	// 전체 차량 목록 조회 -> 메인화면을 위한 페이징 처리
	Page<Vehicle> findAll(Pageable pageable);
	
	// 필드별로 차량 목록을 조회하는 메서드 -> 페이징 처리를 함
	// 제조회사별 차량 목록
	Page<Vehicle> findByBrand(String brand, Pageable pageable);
		
	// 차량구분별 차량 목록
	Page<Vehicle> findByVehicleType(String vehicleType, Pageable pageable);
	
	// 연료타입별 차량 목록
	Page<Vehicle> findByFuelType(String fuelType, Pageable pageable);
	
	// 차량 목록 검색(차량명, 차종)
	Page<Vehicle> findByNameContainingOrVehicleTypeContaining(String name, String vehicleType, Pageable pageable);
	
	
}
