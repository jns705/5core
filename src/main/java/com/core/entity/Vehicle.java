package com.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 차량 기본 정보
 */
@Entity
@Getter
@Setter
public class Vehicle {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 차량 이름 (예: 아반떼, EV6 등) 
    @NotBlank(message = "차량명은 필수 입력 사항입니다.")
	@Column(columnDefinition = "varchar(100)")
    private String name;

    // 브랜드 (현대만)
    @Column(columnDefinition = "varchar(30)")
    private String brand;

    // 연료 타입 ( 가솔린, 디젤, 전기 등)
    @Column(columnDefinition = "varchar(30)")
    private String fuelType;

    // 연식 
    @Column(columnDefinition = "varchar(10)")
    private String year;

    // 배기량 
    @Column(columnDefinition = "varchar(30)")
    private Integer displacement;

    // 차 상태 (SUV, 세단 등) 
    @Column(columnDefinition = "varchar(30)")
    private String vehicleType;
    
    // 기본 가격 
	// 최소값 0, 음수는 사용불가, 전체 11자리, 소수점 2자리
	@NotNull @Min(value=0) @Digits(integer=11, fraction=2)
    private int price;
    
	@Column(columnDefinition = "varchar(30) default 'no_image.jpg'")
	private String fileName;  // 차량이미지의 파일명

    
}
