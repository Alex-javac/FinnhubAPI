package com.itechart.finnhubapi.service;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.UserDto;
import com.itechart.finnhubapi.dto.UserDtoResponse;
import com.itechart.finnhubapi.dto.UserUpdateDto;
import com.itechart.finnhubapi.model.Subscription;
import com.itechart.finnhubapi.model.entity.UserEntity;

import java.util.List;

public interface UserService {
    UserEntity findById(long id);

    void deleteById(long id);

    UserDtoResponse saveUser(UserDto user);

    UserDtoResponse updateUser(UserUpdateDto user);

    List<UserEntity> findAll();

    UserEntity addCompany(String symbol);

    UserEntity lockOrUnlock(Long id, String status);

    List<CompanyDto> getCompaniesFromUser(String username);

    UserEntity findByUsername(String userName);

    UserEntity changeSubscription(Long subscriptionId);

    UserEntity renewSubscription(long month);

    List<CompanyDto> deleteOneCompanyFromUser(String symbol);

    UserEntity findByLoginAndPassword(String login, String password);
}