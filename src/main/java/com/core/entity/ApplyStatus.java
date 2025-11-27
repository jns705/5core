package com.core.entity;

public enum ApplyStatus {
	PENDING("상담대기"),
    IN_PROGRESS("상담진행중"),
    CANCELED("상담취소"),
    COMPLETED("상담완료"),
    PURCHASED("구매완료");
	
	private final String name;
	ApplyStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
