package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.AuthRequest;
import com.itechart.finnhubapi.dto.AuthResponse;
import com.itechart.finnhubapi.dto.MonthDto;
import com.itechart.finnhubapi.dto.SubscriptionIdDto;
import com.itechart.finnhubapi.dto.UserDto;
import com.itechart.finnhubapi.dto.UserDtoResponse;
import com.itechart.finnhubapi.dto.UserUpdateDto;
import com.itechart.finnhubapi.model.StatusUser;
import com.itechart.finnhubapi.model.entity.UserEntity;
import com.itechart.finnhubapi.security.JwtProvider;
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

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MainController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final JwtProvider jwtProvider;

    @PostMapping(value = "/registration")
    public ResponseEntity<UserDtoResponse> registration(@RequestBody @Valid UserDto userDto) {
        UserDtoResponse saveUser = userService.saveUser(userDto);
        return new ResponseEntity<>(saveUser, HttpStatus.CREATED);
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> auth(@RequestBody AuthRequest request) {
        UserEntity userEntity = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(userEntity);
        return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
    }

    @PostMapping(value = "/updateUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_USER_INACTIVE')")
    public ResponseEntity<UserDtoResponse> updateUser(@RequestBody @Valid UserUpdateDto userDto) {
        UserDtoResponse updateUser = userService.updateUser(userDto);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @PostMapping(value = "/lockingUser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> locking(@PathVariable("id") Long id) {
        UserDtoResponse blockedUser = userService.lockOrUnlock(id, StatusUser.BLOCKED.toString());
        String str = String.format("User %s %s " + "\n" +
                " was blocked", blockedUser.getFirstName(), blockedUser.getLastName());
        return new ResponseEntity<>(str, HttpStatus.OK);
    }

    @PostMapping(value = "/unlockingUser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> unlocking(@PathVariable("id") Long id) {
        UserDtoResponse activeUser = userService.lockOrUnlock(id, StatusUser.ACTIVE.toString());
        String str = String.format("User %s %s " + "\n" +
                " was unblocked", activeUser.getFirstName(), activeUser.getLastName());
        return new ResponseEntity<>(str, HttpStatus.OK);
    }

    @PostMapping(value = "/deleteUser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return new ResponseEntity<>(String.format("User with id %d destroyed", id), HttpStatus.OK);
    }

    @PostMapping(value = "/changeSubscription")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDtoResponse> changeSubscription(@RequestBody SubscriptionIdDto subscription) {
        UserDtoResponse user = userService.changeSubscription(subscription.getId());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/renewSubscription")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDtoResponse> renewSubscription(@RequestBody MonthDto month) {
        UserDtoResponse user = userService.renewSubscription(month.getMonth());
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