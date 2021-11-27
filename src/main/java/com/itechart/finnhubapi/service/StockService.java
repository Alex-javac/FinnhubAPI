package com.itechart.finnhubapi.service;

import com.itechart.finnhubapi.dto.financialdto.FinancialStatementDto;
import com.itechart.finnhubapi.dto.metricdto.MetricDto;
import com.itechart.finnhubapi.exceptions.CompanyIsNotOnListException;
import com.itechart.finnhubapi.exceptions.FinancialException;
import com.itechart.finnhubapi.exceptions.MetricException;
import com.itechart.finnhubapi.feignservice.FinnhubClient;
import com.itechart.finnhubapi.model.Subscription;
import com.itechart.finnhubapi.model.entity.SubscriptionTypeEntity;
import com.itechart.finnhubapi.model.entity.UserEntity;
import com.itechart.finnhubapi.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StockService {
    @Value(value = "${feign.token}")
    private String token;
    private final FinnhubClient finnhubClient;
    private final UserService userService;

    public FinancialStatementDto getFinancialResponseEntity(String symbol) {
        UserEntity user = userService.findByUsername(UserUtil.userName());
        SubscriptionTypeEntity subscription = user.getSubscription().getType();
        FinancialStatementDto finance;
        if (Subscription.HIGH.toString().equals(subscription.getName())) {
            if (UserUtil.isCompany(symbol, user)) {
                finance = finnhubClient.getFinance(symbol, token);
                return finance;
            } else {
                throw new CompanyIsNotOnListException(symbol);
            }
        } else {
            throw new FinancialException(subscription.getName());
        }
    }

    public MetricDto getMetricResponseEntity(String symbol) {
        UserEntity user = userService.findByUsername(UserUtil.userName());
        SubscriptionTypeEntity subscription = user.getSubscription().getType();
        MetricDto metric;
        if (Subscription.MEDIUM.toString().equals(subscription.getName()) ||
                Subscription.HIGH.toString().equals(subscription.getName())
        ) {
            if (UserUtil.isCompany(symbol, user)) {
                metric = finnhubClient.getMetric(symbol, token);
                return metric;
            } else {
                throw new CompanyIsNotOnListException(symbol);
            }
        } else {
            throw new MetricException(subscription.getName());
        }
    }
}