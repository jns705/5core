package com.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 차량 옵션 정보
 */
@Entity
@Data
public class VehicleOption {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 옵션명 
    private String optionName;

    // 옵션 가격 
	// 최소값 0, 음수는 사용불가, 전체 11자리, 소수점 2자리
	@NotNull @Min(value=0) @Digits(integer=11, fraction=2)
    private int price;

    // 차량 참조 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    
    
}
