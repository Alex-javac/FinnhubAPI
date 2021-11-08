package com.itechart.finnhubapi.repository;

import com.itechart.finnhubapi.model.QuoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<QuoteEntity, Long> {
}