package com.itechart.finnhubapi.repository;

import com.itechart.finnhubapi.model.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
}
