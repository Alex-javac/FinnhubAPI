package com.itechart.finnhubapi.service;

import com.itechart.finnhubapi.dto.SubscriptionIdDto;
import com.itechart.finnhubapi.exceptions.PayPalException;
import com.itechart.finnhubapi.exceptions.SubscriptionPaidException;
import com.itechart.finnhubapi.exceptions.SubscriptionTypeException;
import com.itechart.finnhubapi.model.Subscription;
import com.itechart.finnhubapi.model.entity.SubscriptionTypeEntity;
import com.itechart.finnhubapi.model.entity.UserEntity;
import com.itechart.finnhubapi.repository.SubscriptionTypeRepository;
import com.itechart.finnhubapi.util.UserUtil;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaypalService {
    private final APIContext apiContext;
    private final UserService userService;
private final SubscriptionTypeRepository typeRepository;
    @Value("${paypal.success.url}")
    private String successUrl;
    @Value("${paypal.cancel.url}")
    private String cancelUrl;
    @Value("${paypal.currency}")
    private String currency;
    @Value("${paypal.method}")
    private String method;
    @Value("${paypal.intent}")
    private String intent;

    public String paymentForSubscription(SubscriptionIdDto subscription) {
        SubscriptionTypeEntity subscriptionType = typeRepository.findById(subscription.getId()).orElseThrow(SubscriptionTypeException::new);
        double price=subscriptionType.getPrice();
        UserEntity user = userService.findByUsername(UserUtil.userName());
        if (!user.getSubscription().getType().getName().equals(subscriptionType.getName())) {
            Payment payment = createPayment(price, subscription.getId());
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) return link.getHref();
            }
        } else {
            throw new SubscriptionPaidException(user.getSubscription().getType().getName());
        }
        return null;
    }

    private Payment createPayment(Double total, Long subscriptionId) {
        try {
            Amount amount = new Amount();
            amount.setCurrency(currency);
            total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
            amount.setTotal(String.valueOf(total));

            Transaction transaction = new Transaction();
            transaction.setDescription(subscriptionId.toString());
            transaction.setAmount(amount);

            List<Transaction> transactions = new ArrayList<>();
            transactions.add(transaction);

            Payer payer = new Payer();
            payer.setPaymentMethod(method);

            Payment payment = new Payment();
            payment.setIntent(intent);
            payment.setPayer(payer);
            payment.setTransactions(transactions);
            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setCancelUrl(cancelUrl);
            redirectUrls.setReturnUrl(successUrl + subscriptionId);
            payment.setRedirectUrls(redirectUrls);

            return payment.create(apiContext);
        } catch (PayPalRESTException e) {
            throw new PayPalException("PayPal isn't working");
        }
    }

    public Payment executePayment(String paymentId, String payerId) {
        try {
            Payment payment = new Payment();
            payment.setId(paymentId);
            PaymentExecution paymentExecute = new PaymentExecution();
            paymentExecute.setPayerId(payerId);
            apiContext.setMaskRequestId(true);
            return payment.execute(apiContext, paymentExecute);
        } catch (PayPalRESTException e) {
            throw new PayPalException("PayPal isn't working");
        }
    }
}