package com.itechart.finnhubapi.service;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.UserDto;
import com.itechart.finnhubapi.dto.UserDtoResponse;
import com.itechart.finnhubapi.dto.UserUpdateDto;
import com.itechart.finnhubapi.model.entity.UserEntity;

import java.util.List;

public interface UserService {
    UserDtoResponse findById(long id);

    void deleteById(long id);

    UserDtoResponse saveUser(UserDto user);

    UserDtoResponse updateUser(UserUpdateDto user);

    List<UserDtoResponse> findAll();

    List<CompanyDto> addCompany(String symbol);

    UserDtoResponse lockOrUnlock(Long id, String status);

    UserEntity findByUsername(String userName);

    UserDtoResponse changeSubscription(Long subscriptionId);

    UserDtoResponse renewSubscription(long month);

    List<CompanyDto> deleteOneCompanyFromUser(String symbol);

    UserEntity findByLoginAndPassword(String login, String password);
}