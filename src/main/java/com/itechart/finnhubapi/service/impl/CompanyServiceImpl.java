package com.itechart.finnhubapi.service.impl;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.CompanyDtoRequest;
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
import com.itechart.finnhubapi.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public CompanyEntity getEntityBySymbol(String symbol) {
        return companyRepository.findBySymbol(symbol).orElseThrow(
                () -> new CompanyNotFoundException(symbol));
    }

    @Transactional
    @Override
    public CompanyDto getBySymbol(String symbol) {
        CompanyEntity bySymbol = companyRepository.findBySymbol(symbol).orElseThrow(
                () -> new CompanyNotFoundException(symbol));
        return CompanyMapper.INSTANCE.companyToCompanyDto(bySymbol);
    }

    @Transactional
    @Override
    public boolean save(List<CompanyDto> companyDtos) {
        companyDtos.stream().limit(100).forEach(companyDto ->
                companyRepository.findBySymbol(companyDto.getSymbol())
                        .orElseGet(() -> {
                            CompanyEntity companyEntity = CompanyMapper.INSTANCE.companyDtoToCompanyEntity(companyDto);
                            CompanyDtoRequest companyDtoRequest = CompanyMapper.INSTANCE.companyToCompanyDtoRequest(companyEntity);
                            quoteMicroserviceClient.saveCompany(companyDtoRequest);
                            return companyRepository.save(companyEntity);
                        }));
        return true;
    }

    @Transactional
    @Override
    public List<CompanyDto> getAllCompanyFromFeign() {
        return serviceFeignClient.getCompany(token);
    }

    @Transactional
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

    @Transactional
    @Override
    public List<QuoteDto> getQuote(String symbol) {
        UserEntity user = userService.findByUsername(UserUtil.userName());
        if (companyUserService.isCompany(symbol, user.getId())) {
            return quoteMicroserviceClient.getQuote(symbol);
        } else {
            throw new CompanyIsNotOnListException(symbol);
        }
    }

    @Transactional
    @Override
    public boolean deleteCompany(String symbol) {
        CompanyEntity company = getEntityBySymbol(symbol);
        Long id = company.getId();
        companyRepository.deleteById(id);
        quoteMicroserviceClient.deleteCompany(symbol);
        return !companyRepository.existsById(id);
    }

    @Transactional
    @Override
    public boolean saveQuote() {
        quoteMicroserviceClient.saveQuote();
        return true;
    }
}