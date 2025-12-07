package com.core.dto;

import com.core.entity.Counseling;

public class CounselingScheduleDTO {
    private Long id;
    private String customerName;
    private String counselingDate;  // "2025-12-07" 이런 형식 그대로
    private String status;
    private String phone;

    public CounselingScheduleDTO(Counseling counseling) {
        this.id = counseling.getId();
        this.customerName = counseling.getCustomer().getMember().getName();
        this.phone = counseling.getCustomer().getMember().getPhone();
        this.status = counseling.getStatus();

        // 🔹 String 필드 그대로 사용
        this.counselingDate = counseling.getCounselingLikeTime();  
    }

    public Long getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getCounselingDate() { return counselingDate; }
    public String getStatus() { return status; }
    public String getPhone() { return phone; }
}
