package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.UserDto;
import com.itechart.finnhubapi.model.CompanyEntity;
import com.itechart.finnhubapi.model.UserEntity;
import com.itechart.finnhubapi.repository.CompanyRepository;
import com.itechart.finnhubapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
public class MainController {
    private final CompanyRepository companyRepository;
    private final UserService userService;

    @Autowired
    public MainController(CompanyRepository companyRepository, UserService userService) {
        this.companyRepository = companyRepository;
        this.userService = userService;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ResponseBody
    public UserEntity registration(@RequestBody UserDto userDto) {
        return userService.saveUser(userDto);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public List<UserEntity> user() {
        return userService.findAll();
    }

    @RequestMapping(value = "/userdata", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @ResponseBody
    public UserDto userdata(@RequestBody String email) {
        return userService.findByEmail(email);
    }

    @RequestMapping(value = "/company", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public List<CompanyEntity> company() {
        List<CompanyEntity> data = companyRepository.findAll();
        return data;
    }

}