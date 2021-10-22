package com.itechart.finnhubapi.controller;


import com.itechart.finnhubapi.model.Subscription;
import com.itechart.finnhubapi.service.PaypalService;
import com.itechart.finnhubapi.service.UserService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaypalController {

    private final UserService userService;
    private final PaypalService service;

    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String home() {
        return "home";
    }

    @PostMapping("/pay")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String payment(@ModelAttribute("subscription") String subscription) {
        double price;
        switch (subscription) {
            case "LOW" -> price = 10.00;
            case "MEDIUM" -> price = 20.00;
            case "HIGH" -> price = 30.00;
            default -> price = 0.00;
        }
        try {
            Payment payment = service.createPayment(price, "USD", "paypal",
                    "sale", "payment description", "http://localhost:8080/" + CANCEL_URL,
                    "http://localhost:8080/" + SUCCESS_URL);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    userService.changeSubscription(Subscription.valueOf(subscription));
                    return "redirect:" + link.getHref();
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping(value = CANCEL_URL)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String cancelPay() {
        return "cancel";
    }

    @GetMapping(value = SUCCESS_URL)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                return "success";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/";
    }
}
