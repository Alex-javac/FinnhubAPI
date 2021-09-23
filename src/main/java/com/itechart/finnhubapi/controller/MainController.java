package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.UserDto;
import com.itechart.finnhubapi.model.CompanyEntity;
import com.itechart.finnhubapi.model.UserEntity;
import com.itechart.finnhubapi.repository.CompanyRepository;
import com.itechart.finnhubapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class MainController {
    private final CompanyRepository companyRepository;
    private final UserService userService;

    @Autowired
    public MainController(CompanyRepository companyRepository, UserService userService) {
        this.companyRepository = companyRepository;
        this.userService = userService;
    }

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String main(Model model, @AuthenticationPrincipal UserDto user) {
        return "index";
    }
    @RequestMapping(value = "/user",method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String user(Model model) {
        List<UserEntity> data = userService.findAll();
        model.addAttribute("data", data);
        return "user";
    }
    @RequestMapping(value = "/company",method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public String company(Model model){
        List<CompanyEntity> data = companyRepository.findAll();
        model.addAttribute("data", data);
        return "comp";
    }

}
