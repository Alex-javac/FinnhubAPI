package com.itechart.finnhubapi.service.impl;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.CompanyDtoRequest;
import com.itechart.finnhubapi.dto.SubscriptionTypeDto;
import com.itechart.finnhubapi.exceptions.CompanyAlreadyOnListException;
import com.itechart.finnhubapi.exceptions.CompanyNotFoundException;
import com.itechart.finnhubapi.exceptions.CompanyUserException;
import com.itechart.finnhubapi.exceptions.CompanyUserOverflowException;
import com.itechart.finnhubapi.mapper.CompanyMapper;
import com.itechart.finnhubapi.model.entity.CompanyEntity;
import com.itechart.finnhubapi.model.entity.CompanyUserEntity;
import com.itechart.finnhubapi.repository.CompanyRepository;
import com.itechart.finnhubapi.repository.CompanyUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyUserService {

    private final CompanyUserRepository companyUserRepository;
    private final CompanyRepository companyRepository;


    public List<CompanyDto> getCompaniesFromUser(Long id) {
        List<CompanyDto> listCompany = new ArrayList<>();
        List<CompanyUserEntity> companyUserEntities = getCompanyUserEntities(id);
        for (CompanyUserEntity companyUserEntity : companyUserEntities) {
            CompanyEntity company = companyRepository.findById(companyUserEntity.getCompanyId()).orElseThrow(() -> new RuntimeException("company not found"));
            listCompany.add(CompanyMapper.INSTANCE.companyToCompanyDto(company));
        }
        return listCompany;
    }

    private List<CompanyUserEntity> getCompanyUserEntities(Long id) {
        return companyUserRepository.findByUsersId(id).orElseThrow(CompanyUserException::new);
    }

    public List<CompanyDto> addCompanyToUser(Long companyId, Long userId, SubscriptionTypeDto subscription) {
        String symbol = companyRepository.findById(companyId).orElseThrow(
                () -> new CompanyNotFoundException(companyId)).getSymbol();
        if (isCompany(symbol, userId)) {
            throw new CompanyAlreadyOnListException(symbol);
        }
        List<CompanyDto> companies = getCompaniesFromUser(userId);
        if (companies.size() >= subscription.getCompanyCount()) {
            throw new CompanyUserOverflowException();
        }
        saveCompanyUser(companyId, userId);
        return getCompaniesFromUser(userId);
    }

    public List<CompanyDto> addListCompaniesToUser(List<CompanyDtoRequest> companies, Long userId, SubscriptionTypeDto subscription) {
        int companiesCount = getCompaniesFromUser(userId).size();
        for (CompanyDtoRequest company : companies) {
            if (companiesCount >= subscription.getCompanyCount()) {
                throw new CompanyUserOverflowException();
            }
            String symbol = companyRepository.findById(company.getId()).orElseThrow(
                    () -> new CompanyNotFoundException(company.getId())).getSymbol();
            if (isCompany(symbol, userId)) {
                throw new CompanyAlreadyOnListException(symbol);
            }
            saveCompanyUser(company.getId(), userId);
            companiesCount++;
        }
        return getCompaniesFromUser(userId);
    }

    public void deleteOneCompanyFromList(Long userId) {
        List<CompanyUserEntity> companyUserEntities = getCompanyUserEntities(userId);
        companyUserRepository.delete(companyUserEntities.get(0));
    }

    public void deleteOneCompanyFromList(Long companyId, Long userId) {
        CompanyUserEntity companyUser = companyUserRepository.findByUsersIdAndCompanyId(userId, companyId).orElseThrow(() -> new RuntimeException("UserCompany not found"));
        companyUserRepository.deleteById(companyUser.getId());
    }

    private void saveCompanyUser(Long companyId, Long userId) {
        CompanyUserEntity companyUserEntity = new CompanyUserEntity();
        companyUserEntity.setCompanyId(companyId);
        companyUserEntity.setUsersId(userId);
        companyUserRepository.save(companyUserEntity);
    }

    public boolean isCompany(String symbol, Long userId) {
        List<CompanyDto> companies = getCompaniesFromUser(userId);
        boolean flag = false;
        for (CompanyDto company : companies) {
            if (company.getSymbol().equals(symbol)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}