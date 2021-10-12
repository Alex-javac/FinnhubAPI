package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.UserDto;
import com.itechart.finnhubapi.model.Subscription;
import com.itechart.finnhubapi.model.UserEntity;
import com.itechart.finnhubapi.service.UserService;
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

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MainController {

    private final UserService userService;


    @PostMapping(value = "/registration")
    public ResponseEntity<UserEntity> registration(@RequestBody UserDto userDto) {
        UserEntity saveUser = userService.saveUser(userDto);
        return new ResponseEntity<>(saveUser, HttpStatus.CREATED);
    }

    @PostMapping(value = "/updateUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<UserEntity> updateUser(@RequestBody UserDto userDto) {
        UserEntity updateUser = userService.updateUser(userDto);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @GetMapping(value = "/lockingUser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserEntity> locking(@PathVariable("id") Long id) {
        UserEntity blockedUser = userService.lockOrUnlock(id, "BLOCKED");
        return new ResponseEntity<>(blockedUser, HttpStatus.OK);
    }

    @GetMapping(value = "/unlockingUser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserEntity> unlocking(@PathVariable("id") Long id) {
        UserEntity activeUser = userService.lockOrUnlock(id, "ACTIVE");
        return new ResponseEntity<>(activeUser, HttpStatus.OK);
    }

    @GetMapping(value = "/deleteUser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return new ResponseEntity<>(String.format("User with id %d destroyed", id), HttpStatus.OK);
    }

    @PostMapping(value = "/changeSubscription")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<UserEntity> changeSubscription(@RequestBody Subscription subscription) {
        UserEntity user = userService.changeSubscription(subscription);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/renewSubscription")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<UserEntity> renewSubscription(@RequestBody long month) {
        UserEntity user = userService.renewSubscription(month);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}