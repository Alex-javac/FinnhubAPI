package com.itechart.finnhubapi.service;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.feignservice.ServiceFeignClient;
import com.itechart.finnhubapi.mapper.CompanyMapper;
import com.itechart.finnhubapi.mapper.QuoteMapper;
import com.itechart.finnhubapi.model.CompanyEntity;
import com.itechart.finnhubapi.model.QuoteEntity;
import com.itechart.finnhubapi.repository.CompanyRepository;
import com.itechart.finnhubapi.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompanyService {
    @Value(value = "${feign.token}")
    private String token;
    private final CompanyRepository companyRepository;
    private final QuoteRepository quoteRepository;
    private final ServiceFeignClient serviceFeignClient;


    public CompanyService(ServiceFeignClient serviceFeignClient, CompanyRepository companyRepository, QuoteRepository quoteRepository) {
        this.serviceFeignClient = serviceFeignClient;
        this.companyRepository = companyRepository;
        this.quoteRepository = quoteRepository;
    }

    public CompanyEntity getEntityBySymbol(String symbol) {
        return  companyRepository.findBySymbol(symbol).orElseThrow(
                () -> new RuntimeException(String.format("company named %s was not found", symbol)));

    }

    public CompanyDto getBySymbol(String symbol) {
        CompanyEntity bySymbol = companyRepository.findBySymbol(symbol).orElseThrow(
                () -> new RuntimeException(String.format("company named %s was not found", symbol)));
        return CompanyMapper.INSTANCE.companyToCompanyDto(bySymbol);
    }

    public void save(List<CompanyDto> companyDtos) {
        companyDtos.forEach(companyDto ->
                companyRepository.findBySymbol(companyDto.getSymbol())
                        .orElseGet(() -> {
                            CompanyEntity companyEntity = CompanyMapper.INSTANCE.companyDtoToCompanyEntity(companyDto);
                            return companyRepository.save(companyEntity);
                        }));
    }

    public List<CompanyDto> getAllCompanyFromFeign() {
        return serviceFeignClient.getCompany(token);
    }

    public List<CompanyEntity> findAll() {
        return companyRepository.findAll();
    }


    public void saveQuote(List<CompanyEntity> company) {
        company.forEach(c -> {
            QuoteEntity quote = QuoteMapper.INSTANCE.quoteDtoToQuoteEntity(
                    serviceFeignClient.getQuote(c.getSymbol(), token));
            quote.setCompany(c);
            quote.setDate(LocalDateTime.now());
            quoteRepository.save(quote);
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
             throw new RuntimeException(e);
            }
        });
    }
}