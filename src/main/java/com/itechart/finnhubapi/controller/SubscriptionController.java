package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.SubscriptionIdDto;
import com.itechart.finnhubapi.model.entity.UserEntity;
import com.itechart.finnhubapi.service.PaypalService;
import com.itechart.finnhubapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/v1/subscription", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SubscriptionController {
    private final PaypalService paypalService;
    private final UserService userService;

    @PostMapping("/payment")
    public String paymentForSubscription(@RequestBody SubscriptionIdDto subscription) {
        return paypalService.paymentForSubscription(subscription);
    }

    @GetMapping("/success/{subscription}")
    public ResponseEntity<String> successPayment(@PathVariable("subscription") long subscription, HttpServletRequest request) {
        paypalService.executePayment(request.getParameter("paymentId"), request.getParameter("PayerID"));
        UserEntity userEntity = userService.changeSubscription(subscription);
        String lastName = userEntity.getLastName();
        String firstName = userEntity.getLastName();
        Double price = userEntity.getSubscription().getType().getPrice();
        String name = userEntity.getSubscription().getType().getName();
        return new ResponseEntity<>(String.format("""
                Payment was successful\s
                User: %s %s\s
                Subscription: %s\s
                Price: %.2f USD""", lastName, firstName, name, price), HttpStatus.OK);
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancelPayment() {
        return new ResponseEntity<>("Payment was canceled", HttpStatus.OK);
    }
}