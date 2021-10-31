package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.financialdto.FinancialStatementDto;
import com.itechart.finnhubapi.dto.metricdto.MetricDto;
import com.itechart.finnhubapi.feignservice.ServiceFeignClient;
import com.itechart.finnhubapi.model.CompanyEntity;
import com.itechart.finnhubapi.model.SubscriptionEntity;
import com.itechart.finnhubapi.model.UserEntity;
import com.itechart.finnhubapi.service.UserService;
import com.itechart.finnhubapi.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = "/api/v1/stock/", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StockRestController {
    @Value(value = "${feign.token}")
    private String token;
    private final ServiceFeignClient serviceFeignClient;
    private final UserService userService;

    @GetMapping("/financials/{symbol}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<FinancialStatementDto> financials(@PathVariable("symbol") String symbol) {
        UserEntity user = userService.findByUsername(UserUtil.userName());
        SubscriptionEntity subscription = user.getSubscription();
        FinancialStatementDto finance;
        if ("HIGH".equals(subscription.getName()) && isCompany(symbol, user)) {
            finance = serviceFeignClient.getFinance(symbol, token);
            return new ResponseEntity<>(finance, HttpStatus.OK);
        } else {
            finance = new FinancialStatementDto();
            return new ResponseEntity<>(finance, HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/metric/{symbol}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<MetricDto> metric(@PathVariable("symbol") String symbol) {
        UserEntity user = userService.findByUsername(UserUtil.userName());
        SubscriptionEntity subscription = user.getSubscription();
        MetricDto metric;
        if (("MEDIUM".equals(subscription.getName()) || "HIGH".equals(subscription.getName())) && isCompany(symbol, user)) {
            metric = serviceFeignClient.getMetric(symbol, token);
            return new ResponseEntity<>(metric, HttpStatus.OK);
        } else {
            metric = new MetricDto();
            return new ResponseEntity<>(metric, HttpStatus.FORBIDDEN);
        }
    }

    private boolean isCompany(String symbol, UserEntity user) {
        List<CompanyEntity> companies = user.getCompanies();
        boolean flag = false;
        for (CompanyEntity company : companies) {
            if (company.getSymbol().equals(symbol)) {
                flag = true;
            }
        }
        return flag;
    }
}