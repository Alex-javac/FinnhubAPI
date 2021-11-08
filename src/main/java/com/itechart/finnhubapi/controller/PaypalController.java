package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.service.PaypalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.itechart.finnhubapi.service.PaypalService.CANCEL_URL;
import static com.itechart.finnhubapi.service.PaypalService.SUCCESS_URL;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaypalController {
    private final PaypalService service;

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String home() {
        return "home";
    }

    @PostMapping("/pay")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String payment(@ModelAttribute("subscription") String subscription) {
        return service.getPaymentResult(subscription);
    }

    @GetMapping(value = CANCEL_URL)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String cancelPay() {
        return "cancel";
    }

    @GetMapping(value = SUCCESS_URL)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        return service.getSuccessPayResult(paymentId, payerId);
    }
}