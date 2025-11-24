package com.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.core.entity.Counseling;

public interface CounselingRepository extends JpaRepository<Counseling, Long> {
}

