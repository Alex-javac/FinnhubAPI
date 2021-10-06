package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.UserDto;
import com.itechart.finnhubapi.model.UserEntity;
import com.itechart.finnhubapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MainController {

    private final UserService userService;


    @PostMapping(value = "/registration")
    @ResponseBody
    public UserEntity registration(@RequestBody UserDto userDto) {
        return userService.saveUser(userDto);
    }

    @PostMapping(value = "/update")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @ResponseBody
    public UserEntity update(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @GetMapping(value = "/locking/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public UserEntity locking(@PathVariable("id") Long id) {
        return userService.lockOrUnlock(id, "BLOCKED");
    }

    @GetMapping(value = "/unlocking/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public UserEntity unlocking(@PathVariable("id") Long id) {
        return userService.lockOrUnlock(id, "ACTIVE");
    }

    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public String delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return String.format("User with id %d destroyed", id);
    }

}