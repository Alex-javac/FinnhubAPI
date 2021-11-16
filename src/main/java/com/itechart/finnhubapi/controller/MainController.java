package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.MonthDto;
import com.itechart.finnhubapi.dto.SubscriptionNameDto;
import com.itechart.finnhubapi.dto.UserDto;
import com.itechart.finnhubapi.dto.UserDtoResponse;
import com.itechart.finnhubapi.model.Status;
import com.itechart.finnhubapi.model.UserEntity;
import com.itechart.finnhubapi.service.SubscriptionService;
import com.itechart.finnhubapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MainController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;

    @PostMapping(value = "/registration")
    public ResponseEntity<UserDtoResponse> registration(@RequestBody UserDto userDto) {
        UserDtoResponse saveUser = userService.saveUser(userDto);
        return new ResponseEntity<>(saveUser, HttpStatus.CREATED);
    }

    @PostMapping(value = "/updateUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_USER_INACTIVE')")
    public ResponseEntity<UserDtoResponse> updateUser(@RequestBody UserDto userDto) {
        UserDtoResponse updateUser = userService.updateUser(userDto);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @PostMapping(value = "/lockingUser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserEntity> locking(@PathVariable("id") Long id) {
        UserEntity blockedUser = userService.lockOrUnlock(id, Status.BLOCKED.toString());
        return new ResponseEntity<>(blockedUser, HttpStatus.OK);
    }

    @PostMapping(value = "/unlockingUser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserEntity> unlocking(@PathVariable("id") Long id) {
        UserEntity activeUser = userService.lockOrUnlock(id, Status.ACTIVE.toString());
        return new ResponseEntity<>(activeUser, HttpStatus.OK);
    }

    @PostMapping(value = "/deleteUser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return new ResponseEntity<>(String.format("User with id %d destroyed", id), HttpStatus.OK);
    }

    @PostMapping(value = "/changeSubscription")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserEntity> changeSubscription(@RequestBody SubscriptionNameDto subscription) {
        UserEntity user = userService.changeSubscription(subscription.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/renewSubscription")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserEntity> renewSubscription(@RequestBody MonthDto month) {
        UserEntity user = userService.renewSubscription(month.getMonth());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/verificationSubscriptions")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> verificationSubscriptions() {
        Map<String, Long> result = subscriptionService.verificationSubscriptions();
        String str = String.format("Blocked users: %d" + "\n" +
                "Users warned: %d", result.get("blocked"), result.get("warned"));
        return new ResponseEntity<>(str, HttpStatus.OK);
    }
}