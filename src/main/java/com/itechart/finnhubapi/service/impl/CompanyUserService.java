package com.itechart.finnhubapi.service.impl;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.SubscriptionTypeDto;
import com.itechart.finnhubapi.exceptions.CompanyAlreadyOnListException;
import com.itechart.finnhubapi.exceptions.CompanyNotFoundException;
import com.itechart.finnhubapi.exceptions.CompanyUserException;
import com.itechart.finnhubapi.mapper.CompanyMapper;
import com.itechart.finnhubapi.model.Subscription;
import com.itechart.finnhubapi.model.entity.CompanyEntity;
import com.itechart.finnhubapi.model.entity.CompanyUserEntity;
import com.itechart.finnhubapi.model.entity.SubscriptionTypeEntity;
import com.itechart.finnhubapi.repository.CompanyRepository;
import com.itechart.finnhubapi.repository.CompanyUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyUserService {

    private final CompanyUserRepository companyUserRepository;
    private final CompanyRepository companyRepository;

    @Transactional
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
        List<CompanyUserEntity> companyUserEntities = companyUserRepository.findByUsersId(id).orElseThrow(CompanyUserException::new);
        return companyUserEntities;
    }

    @Transactional
    public List<CompanyDto> addCompanyToUser(String symbol, Long userID, SubscriptionTypeDto subscription) {
        if (isCompany(symbol, userID)) {
            throw new CompanyAlreadyOnListException(symbol);
        }
        List<CompanyDto> companies = getCompaniesFromUser(userID);
        if (Subscription.LOW.toString().equals(subscription.getName())) {
            if (companies.size() >= 2) {
                deleteOneCompanyFromList(userID);
            }
            saveCompanyUser(symbol, userID);
        } else if (Subscription.MEDIUM.toString().equals(subscription.getName()) ||
                Subscription.HIGH.toString().equals(subscription.getName())) {
            if (companies.size() >= 3) {
                deleteOneCompanyFromList(userID);
            }
            saveCompanyUser(symbol, userID);
        }
        return getCompaniesFromUser(userID);
    }

    public void deleteOneCompanyFromList(Long userId) {
        List<CompanyUserEntity> companyUserEntities = getCompanyUserEntities(userId);
        companyUserRepository.delete(companyUserEntities.get(0));
    }

    public void deleteOneCompanyFromList(Long companyId, Long userId) {
        CompanyUserEntity companyUser = companyUserRepository.findByUsersIdAndCompanyId(userId, companyId).orElseThrow(() -> new RuntimeException("UserCompany not found"));
        companyUserRepository.deleteById(companyUser.getId());
    }

    private void saveCompanyUser(String symbol, Long userId) {
        CompanyEntity company = companyRepository.findBySymbol(symbol).orElseThrow(
                () -> new CompanyNotFoundException(symbol));
        CompanyUserEntity companyUserEntity = new CompanyUserEntity();
        companyUserEntity.setCompanyId(company.getId());
        companyUserEntity.setUsersId(userId);
        companyUserRepository.save(companyUserEntity);
    }

    public boolean isCompany(String symbol, Long userId) {
        List<CompanyDto> companies = getCompaniesFromUser(userId);
        boolean flag = false;
        for (CompanyDto company : companies) {
            if (company.getSymbol().equals(symbol)) {
                flag = true;
            }
        }
        return flag;
    }
}