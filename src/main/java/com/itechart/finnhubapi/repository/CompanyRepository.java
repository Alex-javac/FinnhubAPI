package com.itechart.finnhubapi.repository;

import com.itechart.finnhubapi.model.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    Optional<CompanyEntity> findBySymbol(String symbol);
}
