package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.SubscriptionNameDto;
import com.itechart.finnhubapi.model.Subscription;
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
    public String paymentForSubscription(@RequestBody SubscriptionNameDto subscription) {
        return paypalService.paymentForSubscription(subscription);
    }

    @GetMapping("/success/{subscription}")
    public ResponseEntity<String> successPayment(@PathVariable("subscription") String subscription, HttpServletRequest request){
        paypalService.executePayment(request.getParameter("paymentId"), request.getParameter("PayerID"));
        userService.changeSubscription(Subscription.valueOf(subscription.toUpperCase()));
        return new ResponseEntity<>("Payment was successful", HttpStatus.OK);
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancelPayment() {
        return new ResponseEntity<>("Payment was canceled", HttpStatus.OK);
    }
}