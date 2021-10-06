package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.model.UserEntity;
import com.itechart.finnhubapi.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/getAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public List<UserEntity> getAll() {
        return userService.findAll();
    }


    @GetMapping(value = "/getOne/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @ResponseBody
    public UserEntity getOne(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @PostMapping(value = "/addCompany")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @ResponseBody
    public UserEntity addCompany(@RequestParam Long id,
                                 @RequestBody List<String> symbol ) {
        return userService.addCompany(id, symbol);
    }

    @GetMapping(value = "/getCompany/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @ResponseBody
    public List<CompanyDto> userCompany(@PathVariable("id") Long id) {
        return userService.getCompaniesFromUser(id);
    }


}
