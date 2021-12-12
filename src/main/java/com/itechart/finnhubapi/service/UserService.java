package com.itechart.finnhubapi.service;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.CompanyDtoRequest;
import com.itechart.finnhubapi.dto.UserDto;
import com.itechart.finnhubapi.dto.UserDtoResponse;
import com.itechart.finnhubapi.dto.UserUpdateDto;
import com.itechart.finnhubapi.model.entity.UserEntity;

import java.util.List;

public interface UserService {
    UserDtoResponse findById(long id);

    public UserEntity findUserEntityById(long id);

    void deleteById(long id);

    UserDtoResponse saveUser(UserDto user);

    UserDtoResponse updateUser(UserUpdateDto user, Long userId);

    List<UserDtoResponse> findAll();

    List<CompanyDto> addCompany(Long companyId, Long userId);

    List<CompanyDto> addListCompaniesToUser(List<CompanyDtoRequest> companies, Long userId);

    UserDtoResponse lockOrUnlock(Long id, String status);

    UserEntity findByUsername(String userName);

    UserDtoResponse changeSubscription(Long subscriptionId, Long userId);

    UserDtoResponse renewSubscription(long month, Long userId);

    List<CompanyDto> deleteOneCompanyFromUser(Long companyId, Long userId);

    UserEntity findByLoginAndPassword(String login, String password);
}