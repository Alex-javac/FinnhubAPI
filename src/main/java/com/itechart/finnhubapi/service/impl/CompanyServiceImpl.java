package com.itechart.finnhubapi.service.impl;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.CompanyDtoRequest;
import com.itechart.finnhubapi.dto.QuoteDto;
import com.itechart.finnhubapi.exceptions.CompanyIsNotOnListException;
import com.itechart.finnhubapi.exceptions.CompanyNotFoundException;
import com.itechart.finnhubapi.exceptions.NoDataFoundException;
import com.itechart.finnhubapi.feignservice.MicroserviceFeignClient;
import com.itechart.finnhubapi.feignservice.ServiceFeignClient;
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

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyServiceImpl implements CompanyService {
    @Value(value = "${feign.token}")
    private String token;
    private final CompanyRepository companyRepository;
    private final UserService userService;
    private final ServiceFeignClient serviceFeignClient;
    private final MicroserviceFeignClient microserviceFeignClient;

    public CompanyEntity getEntityBySymbol(String symbol) {
        return companyRepository.findBySymbol(symbol).orElseThrow(
                () -> new CompanyNotFoundException(symbol));
    }

    public CompanyDto getBySymbol(String symbol) {
        CompanyEntity bySymbol = companyRepository.findBySymbol(symbol).orElseThrow(
                () -> new CompanyNotFoundException(symbol));
        return CompanyMapper.INSTANCE.companyToCompanyDto(bySymbol);
    }

    public boolean save(List<CompanyDto> companyDtos) {
        companyDtos.stream().limit(100).forEach(companyDto ->
                companyRepository.findBySymbol(companyDto.getSymbol())
                        .orElseGet(() -> {
                            CompanyEntity companyEntity = CompanyMapper.INSTANCE.companyDtoToCompanyEntity(companyDto);
                            CompanyDtoRequest companyDtoRequest = CompanyMapper.INSTANCE.companyToCompanyDtoRequest(companyEntity);
                            microserviceFeignClient.saveCompany(companyDtoRequest);
                            return companyRepository.save(companyEntity);
                        }));
        return true;
    }

    public List<CompanyDto> getAllCompanyFromFeign() {
        return serviceFeignClient.getCompany(token);
    }

    public List<CompanyEntity> findAll() {
        List<CompanyEntity> companyEntities = companyRepository.findAll();
        if (companyEntities.isEmpty()) {
            throw new NoDataFoundException();
        }
        return companyEntities;
    }

    public List<QuoteDto> getQuote(String symbol) {
        UserEntity user = userService.findByUsername(UserUtil.userName());
        if (UserUtil.isCompany(symbol, user)) {
            return microserviceFeignClient.getQuote(symbol);
        } else {
            throw new CompanyIsNotOnListException(symbol);
        }
    }

    public boolean deleteCompany(String symbol) {
        CompanyEntity company = getEntityBySymbol(symbol);
        Long id = company.getId();
        companyRepository.deleteById(id);
        microserviceFeignClient.deleteCompany(symbol);
        return !companyRepository.existsById(id);
    }

    public boolean saveQuote() {
        microserviceFeignClient.saveQuote();
        return true;
    }
}