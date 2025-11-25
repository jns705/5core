package com.core.repository;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
	
	// 키워드, 차종, 연료타입별 조회 (사이드 메뉴 라디오 버튼)
	@Query("""
			SELECT v FROM Vehicle v
			WHERE (:keyword IS NULL OR v.name LIKE CONCAT('%', :keyword, '%'))
			AND (:type IS NULL OR v.vehicleType = :type)
			AND (:fuel IS NULL OR v.fuelType = :fuel)
			""")
	Page<Vehicle> searchVehicle(@Param("keyword") String keyword, @Param("type") String type, @Param("fuel") String fuel, Pageable pageable);
	
}
