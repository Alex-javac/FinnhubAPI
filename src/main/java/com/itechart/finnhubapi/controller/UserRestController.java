package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.CompanyDtoRequest;
import com.itechart.finnhubapi.dto.UserDtoResponse;
import com.itechart.finnhubapi.service.UserService;
import com.itechart.finnhubapi.service.impl.CompanyUserService;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserRestController {

    private final UserService userService;
    private final CompanyUserService companyUserService;
    private final UserUtil userUtil;

    @GetMapping(value = "/getAllUsers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDtoResponse>> getAllUsers() {
        List<UserDtoResponse> allUsers = userService.findAll();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping(value = "/getOneUser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDtoResponse> getOneUser(@PathVariable("id") Long id) {
        UserDtoResponse userById = userService.findById(id);
        return new ResponseEntity<>(userById, HttpStatus.OK);
    }

    @GetMapping(value = "/getOneUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<UserDtoResponse> getOneUser(HttpServletRequest request) {
        Long userID = userUtil.userID(request);
        UserDtoResponse userById = userService.findById(userID);
        return new ResponseEntity<>(userById, HttpStatus.OK);
    }

    @PostMapping(value = "/addCompanyToUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<CompanyDto>> addCompanyToUser(@RequestBody CompanyDtoRequest symbol, HttpServletRequest request) {
        Long userID = userUtil.userID(request);
        List<CompanyDto> userCompany = userService.addCompany(symbol.getId(), userID);
        return new ResponseEntity<>(userCompany, HttpStatus.OK);
    }

    @PostMapping(value = "/addListCompaniesToUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<CompanyDto>> addListCompaniesToUser(@RequestBody List<CompanyDtoRequest> companies, HttpServletRequest request) {
        Long userID = userUtil.userID(request);
        List<CompanyDto> userCompany = userService.addListCompaniesToUser(companies, userID);
        return new ResponseEntity<>(userCompany, HttpStatus.OK);
    }

    @GetMapping(value = "/getCompanyFromUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<CompanyDto>> getCompanyFromUser(HttpServletRequest request) {
        Long userID = userUtil.userID(request);
        List<CompanyDto> companiesFromUser = companyUserService.getCompaniesFromUser(userID);
        return new ResponseEntity<>(companiesFromUser, HttpStatus.OK);
    }

    @PostMapping(value = "/deleteOneCompanyFromUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<CompanyDto>> deleteOneCompanyFromUser(@RequestBody CompanyDtoRequest symbol, HttpServletRequest request) {
        Long userID = userUtil.userID(request);
        List<CompanyDto> companiesFromUser = userService.deleteOneCompanyFromUser(symbol.getId(), userID);
        return new ResponseEntity<>(companiesFromUser, HttpStatus.OK);
    }
}