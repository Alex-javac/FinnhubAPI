package com.itechart.finnhubapi.repository;

import com.itechart.finnhubapi.model.entity.CompanyEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    private final CompanyEntity company = new CompanyEntity();

    @BeforeEach
    void setUp() {
        company.setSymbol("Test");
        company.setMic("OOTC");
        company.setType("Common Stock");
        company.setFigi("BBG000BJL537");
        company.setCurrency("USD");
        company.setDescription("JOHN WOOD GROUP PLC");
        company.setDisplaySymbol("Test");
    }

    @Test
    void findBySymbol() {
        CompanyEntity companyEntity = companyRepository.save(company);
        Optional<CompanyEntity> optionalCompany = companyRepository.findBySymbol(companyEntity.getSymbol());
        assertTrue(optionalCompany.isPresent());
        optionalCompany.ifPresent(entity -> {
            assertEquals(company.getSymbol(), entity.getSymbol());
        });
    }

    @Test
    void findAll() {
        final int COMPANIES_COUNT_IN_DATABASE = 100;
        List<CompanyEntity> resultList = companyRepository.findAll();
        assertThat(resultList).isNotNull();
        assertThat(resultList.size()).isEqualTo(COMPANIES_COUNT_IN_DATABASE);
    }

    @Test
    void findById() {
        CompanyEntity companyEntity = companyRepository.save(company);
        Optional<CompanyEntity> optionalCompany = companyRepository.findById(companyEntity.getId());
        assertTrue(optionalCompany.isPresent());
        optionalCompany.ifPresent(entity -> {
            assertEquals(company.getSymbol(), entity.getSymbol());
        });
    }

    @Test
    void save() {
        companyRepository.save(company);
        assertNotNull(company.getId());
    }

    @Test
    void deleteById() {
        CompanyEntity companyEntity = companyRepository.save(company);
        companyRepository.deleteById(companyEntity.getId());
        CompanyEntity companyNull = companyRepository.findById(companyEntity.getId()).orElse(null);
        assertNull(companyNull);
    }
}