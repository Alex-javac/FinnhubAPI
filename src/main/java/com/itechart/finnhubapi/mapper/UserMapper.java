package com.itechart.finnhubapi.mapper;

import com.itechart.finnhubapi.dto.UserDto;
import com.itechart.finnhubapi.dto.UserDtoResponse;
import com.itechart.finnhubapi.dto.UserUpdateDto;
import com.itechart.finnhubapi.model.entity.UserEntity;
import com.itechart.finnhubapi.security.CustomUserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(UserEntity user);

    UserEntity userDtoToUserEntity(UserDto userDto);

    UserDtoResponse userToUserDtoResponse(UserEntity user);

    UserEntity userDtoResponseToUserEntity(UserDtoResponse userDtoResponse);

    UserUpdateDto userToUserUpdateDto(UserEntity user);
}