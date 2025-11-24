package com.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 상담 엔터티
 * - 고객이 신청 → 딜러가 상태 변경
 */  
@Entity
@Getter
@Setter
public class Counseling {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 상담 제목 (회원 id님이 상담 신청했습니다)
    @Column(columnDefinition = "varchar(50)")
    private String title;

    // 상담 내용 
    @Column(columnDefinition = "text")
    private String content;

    // 상담 상태 (상담신청/상담완료/판매완료) 
    @Column(columnDefinition = "varchar(20) default '상담신청'")
    private String status;

    // 상담 신청 고객 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // 담당 딜러 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dealer_id")
    private Dealer dealer;
    
}
