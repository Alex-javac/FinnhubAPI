package com.itechart.finnhubapi.service;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.UserDto;
import com.itechart.finnhubapi.dto.UserDtoResponse;
import com.itechart.finnhubapi.model.Subscription;
import com.itechart.finnhubapi.model.UserEntity;

import java.util.List;

public interface UserService {
    UserEntity findById(long id);

    void deleteById(long id);

    UserDtoResponse saveUser(UserDto user);

    UserDtoResponse updateUser(UserDto user);

    List<UserEntity> findAll();

    UserEntity addCompany(String symbol);

    UserEntity lockOrUnlock(Long id, String status);

    List<CompanyDto> getCompaniesFromUser(String username);

    UserEntity findByUsername(String userName);

    UserEntity changeSubscription(Subscription subscription);

    UserEntity renewSubscription(long month);

    List<CompanyDto> deleteOneCompanyFromUser(String symbol);
}