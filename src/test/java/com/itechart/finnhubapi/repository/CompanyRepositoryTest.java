package com.itechart.finnhubapi.repository;

import com.itechart.finnhubapi.model.CompanyEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    private final CompanyEntity company = new CompanyEntity();

    @BeforeEach
    void setUp() {
        company.setSymbol("Test");
        company.setMic("OOTC");
        company.setType("Common Stock");
        company.setId(2L);
        company.setFigi("BBG000BJL537");
        company.setCurrency("USD");
        company.setDescription("JOHN WOOD GROUP PLC");
        company.setDisplaySymbol("Test");

    }

    @Test
    void findBySymbol() {

    }
    @Test
    void findAll() {

    }

    @Test
    void findById() {

    }

    @Test
    void save() {

    }

    @Test
    void deleteById() {

    }
}