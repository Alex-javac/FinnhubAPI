package com.itechart.finnhubapi.service.impl;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.CompanySymbolDto;
import com.itechart.finnhubapi.dto.QuoteDto;
import com.itechart.finnhubapi.exceptions.CompanyIsNotOnListException;
import com.itechart.finnhubapi.exceptions.CompanyNotFoundException;
import com.itechart.finnhubapi.exceptions.NoDataFoundException;
import com.itechart.finnhubapi.feignservice.QuoteMicroserviceClient;
import com.itechart.finnhubapi.feignservice.FinnhubClient;
import com.itechart.finnhubapi.mapper.CompanyMapper;
import com.itechart.finnhubapi.model.entity.CompanyEntity;
import com.itechart.finnhubapi.model.entity.UserEntity;
import com.itechart.finnhubapi.repository.CompanyRepository;
import com.itechart.finnhubapi.service.CompanyService;
import com.itechart.finnhubapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyServiceImpl implements CompanyService {
    @Value(value = "${feign.token}")
    private String token;
    private final CompanyRepository companyRepository;
    private final UserService userService;
    private final FinnhubClient serviceFeignClient;
    private final QuoteMicroserviceClient quoteMicroserviceClient;
    private final CompanyUserService companyUserService;

@Override
    public CompanyEntity getEntityById(Long id) {
        return companyRepository.findById(id).orElseThrow(
                () -> new CompanyNotFoundException(id));
    }

    @Override
    public CompanyDto getBySymbol(String symbol) {
        CompanyEntity bySymbol = companyRepository.findBySymbol(symbol).orElseThrow(
                () -> new CompanyNotFoundException(symbol));
        return CompanyMapper.INSTANCE.companyToCompanyDto(bySymbol);
    }

    @Override
    public boolean save(List<CompanyDto> companyDtos) {
        companyDtos.stream().limit(50).forEach(companyDto ->
                companyRepository.findBySymbol(companyDto.getSymbol())
                        .orElseGet(() -> {
                            CompanyEntity companyEntity = CompanyMapper.INSTANCE.companyDtoToCompanyEntity(companyDto);
                            CompanySymbolDto companySymbolDto = CompanyMapper.INSTANCE.companyToCompanySymbolDto(companyEntity);
                            quoteMicroserviceClient.saveCompany(companySymbolDto);
                            return companyRepository.save(companyEntity);
                        }));
        return true;
    }

    @Override
    public List<CompanyDto> getAllCompanyFromFeign() {
        return serviceFeignClient.getCompany(token);
    }

    @Override
    public List<CompanyDto> findAll() {
        List<CompanyDto> listCompany = new ArrayList<>();
        List<CompanyEntity> companyEntities = companyRepository.findAll();
        if (companyEntities.isEmpty()) {
            throw new NoDataFoundException();
        }
        for (CompanyEntity companyEntity : companyEntities) {
            listCompany.add(CompanyMapper.INSTANCE.companyToCompanyDto(companyEntity));
        }
        return listCompany;
    }

    @Override
    public List<QuoteDto> getQuote(String symbol, Long userId) {
        UserEntity user = userService.findUserEntityById(userId);
        if (companyUserService.isCompany(symbol, user.getId())) {
            return quoteMicroserviceClient.getQuote(symbol);
        } else {
            throw new CompanyIsNotOnListException(symbol);
        }
    }

    @Override
    public boolean deleteCompany(Long id) {
        CompanyEntity company = getEntityById(id);
        String symbol = company.getSymbol();
        companyRepository.deleteById(id);
        quoteMicroserviceClient.deleteCompany(symbol);
        return !companyRepository.existsById(id);
    }

    @Override
    public boolean saveQuote() {
        quoteMicroserviceClient.saveQuote();
        return true;
    }
}