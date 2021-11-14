package com.itechart.finnhubapi.repository;

import com.itechart.finnhubapi.model.CompanyEntity;
import com.itechart.finnhubapi.model.QuoteEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
class QuoteRepositoryTest {
    @Autowired
    QuoteRepository quoteRepository;
    @Autowired
    private CompanyRepository companyRepository;

    private final QuoteEntity quote = new QuoteEntity();
    private final CompanyEntity company = new CompanyEntity();

    @BeforeEach
    void setUp() {
        quote.setC(3.2);
        quote.setD(0.2199);
        quote.setDp(7.3789);
        quote.setH(3.2);
        quote.setL(3.2);
        quote.setO(3.2);
        quote.setPc(2.9801);
        quote.setT(1633708145);
        quote.setDate(LocalDateTime.now());
        company.setSymbol("Test");
        company.setMic("OOTC");
        company.setType("Common Stock");
        company.setFigi("BBG000BJL537");
        company.setCurrency("USD");
        company.setDescription("JOHN WOOD GROUP PLC");
        company.setDisplaySymbol("Test");
        CompanyEntity savedCompany = companyRepository.save(company);
        quote.setCompany(savedCompany);
    }

    @Test
    void save() {
        QuoteEntity quoteEntity = quoteRepository.save(quote);
        assertNotNull(quoteEntity.getId());
    }

    @Test
    void findAll() {
        quoteRepository.save(quote);
        List<QuoteEntity> resultList = quoteRepository.findAll();
        assertThat(resultList).isNotNull();
        assertThat(resultList.size()).isEqualTo(1);
    }

    @Test
    void findById() {
        CompanyEntity savedCompany = companyRepository.save(company);
        quote.setCompany(savedCompany);
        QuoteEntity quoteEntity = quoteRepository.save(quote);
        Optional<QuoteEntity> optionalQuote = quoteRepository.findById(quoteEntity.getId());
        assertTrue(optionalQuote.isPresent());
        optionalQuote.ifPresent(entity -> {
            assertEquals(savedCompany, entity.getCompany());
        });
    }

    @Test
    void deleteById() {
        QuoteEntity quoteEntity = quoteRepository.save(quote);
        quoteRepository.deleteById(quoteEntity.getId());
        QuoteEntity quoteNull = quoteRepository.findById(quoteEntity.getId()).orElse(null);
        assertNull(quoteNull);
    }
}