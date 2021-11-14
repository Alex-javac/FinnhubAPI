package com.itechart.finnhubapi.service;

import com.itechart.finnhubapi.dto.financialdto.FinancialStatementDto;
import com.itechart.finnhubapi.dto.metricdto.MetricDto;
import com.itechart.finnhubapi.feignservice.ServiceFeignClient;
import com.itechart.finnhubapi.model.CompanyEntity;
import com.itechart.finnhubapi.model.SubscriptionEntity;
import com.itechart.finnhubapi.model.UserEntity;
import com.itechart.finnhubapi.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StockService {
    @Value(value = "${feign.token}")
    private String token;
    private final ServiceFeignClient serviceFeignClient;
    private final UserService userService;

    public ResponseEntity<FinancialStatementDto> getFinancialResponseEntity(String symbol) {
        UserEntity user = userService.findByUsername(UserUtil.userName());
        SubscriptionEntity subscription = user.getSubscription();
        FinancialStatementDto finance;
        if ("HIGH".equals(subscription.getName()) && UserUtil.isCompany(symbol, user)) {
            finance = serviceFeignClient.getFinance(symbol, token);
            return new ResponseEntity<>(finance, HttpStatus.OK);
        } else {
            finance = new FinancialStatementDto();
            return new ResponseEntity<>(finance, HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<MetricDto> getMetricResponseEntity(String symbol) {
        UserEntity user = userService.findByUsername(UserUtil.userName());
        SubscriptionEntity subscription = user.getSubscription();
        MetricDto metric;
        if (("MEDIUM".equals(subscription.getName()) || "HIGH".equals(subscription.getName())) && UserUtil.isCompany(symbol, user)) {
            metric = serviceFeignClient.getMetric(symbol, token);
            return new ResponseEntity<>(metric, HttpStatus.OK);
        } else {
            metric = new MetricDto();
            return new ResponseEntity<>(metric, HttpStatus.FORBIDDEN);
        }
    }
}