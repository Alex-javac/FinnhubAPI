package com.itechart.finnhubapi.service;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.feignservice.ServiceFeignClient;
import com.itechart.finnhubapi.mapper.CompanyMapper;
import com.itechart.finnhubapi.mapper.QuoteMapper;
import com.itechart.finnhubapi.model.CompanyEntity;
import com.itechart.finnhubapi.model.QuoteEntity;
import com.itechart.finnhubapi.repository.CompanyRepository;
import com.itechart.finnhubapi.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyService {
    @Value(value = "${feign.token}")
    private String token;
    private final CompanyRepository companyRepository;
    private final QuoteRepository quoteRepository;
    private final ServiceFeignClient serviceFeignClient;

    public CompanyEntity getEntityBySymbol(String symbol) {
        return companyRepository.findBySymbol(symbol).orElseThrow(
                () -> new RuntimeException(String.format("company named %s was not found", symbol)));
    }

    public CompanyDto getBySymbol(String symbol) {
        CompanyEntity bySymbol = companyRepository.findBySymbol(symbol).orElseThrow(
                () -> new RuntimeException(String.format("company named %s was not found", symbol)));
        return CompanyMapper.INSTANCE.companyToCompanyDto(bySymbol);
    }

    public boolean save(List<CompanyDto> companyDtos) {
        companyDtos.stream().limit(100).forEach(companyDto ->
                companyRepository.findBySymbol(companyDto.getSymbol())
                        .orElseGet(() -> {
                            CompanyEntity companyEntity = CompanyMapper.INSTANCE.companyDtoToCompanyEntity(companyDto);
                            return companyRepository.save(companyEntity);
                        }));
        return true;
    }

    public List<CompanyDto> getAllCompanyFromFeign() {
        return serviceFeignClient.getCompany(token);
    }

    public List<CompanyEntity> findAll() {
        return companyRepository.findAll();
    }


    public boolean saveQuote(List<CompanyEntity> company) {
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
        return true;
    }

    public boolean deleteCompany(String symbol) {
        return companyRepository.deleteBySymbol(symbol);
    }
}