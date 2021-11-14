package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.CompanyDtoRequest;
import com.itechart.finnhubapi.dto.UserDtoResponse;
import com.itechart.finnhubapi.mapper.UserMapper;
import com.itechart.finnhubapi.model.UserEntity;
import com.itechart.finnhubapi.service.UserService;
import com.itechart.finnhubapi.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserRestController {

    private final UserService userService;

    @GetMapping(value = "/getAllUsers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> allUsers = userService.findAll();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping(value = "/getOneUser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserEntity> getOneUser(@PathVariable("id") Long id) {
        UserEntity userById = userService.findById(id);
        return new ResponseEntity<>(userById, HttpStatus.OK);
    }

    @GetMapping(value = "/getOneUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<UserDtoResponse> getOneUser() {
        UserDtoResponse userById = UserMapper.INSTANCE.userToUserDtoResponse(userService.findByUsername(UserUtil.userName()));
        return new ResponseEntity<>(userById, HttpStatus.OK);
    }

    @PostMapping(value = "/addCompanyToUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<UserEntity> addCompanyToUser(@RequestBody CompanyDtoRequest symbol) {
        UserEntity userEntity = userService.addCompany(symbol.getSymbol());
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

    @GetMapping(value = "/getCompanyFromUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<CompanyDto>> getCompanyFromUser() {
        List<CompanyDto> companiesFromUser = userService.getCompaniesFromUser(UserUtil.userName());
        return new ResponseEntity<>(companiesFromUser, HttpStatus.OK);
    }

    @PostMapping(value = "/deleteOneCompanyFromUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<CompanyDto>> deleteOneCompanyFromUser(@RequestBody CompanyDtoRequest symbol) {
        List<CompanyDto> companiesFromUser = userService.deleteOneCompanyFromUser(symbol.getSymbol());
        return new ResponseEntity<>(companiesFromUser, HttpStatus.OK);
    }
}