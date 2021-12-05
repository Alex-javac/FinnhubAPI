package com.itechart.finnhubapi.mapper;

import com.itechart.finnhubapi.dto.*;
import com.itechart.finnhubapi.model.entity.RoleEntity;
import com.itechart.finnhubapi.model.entity.SubscriptionEntity;
import com.itechart.finnhubapi.model.entity.SubscriptionTypeEntity;
import com.itechart.finnhubapi.model.entity.UserEntity;
import com.itechart.finnhubapi.security.CustomUserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(UserEntity user);

    UserEntity userDtoToUserEntity(UserDto userDto);

    SubscriptionDto subscriptionToSubscriptionDto(SubscriptionEntity subscription);

    SubscriptionTypeDto subscriptionTypeToSubscriptionTypeDto(SubscriptionTypeEntity subscriptionType);

    RoleDto roleEntityToRoleDto(RoleEntity roleEntity);

    UserDtoResponse userToUserDtoResponse(UserEntity user);

    UserUpdateDto userToUserUpdateDto(UserEntity user);
}