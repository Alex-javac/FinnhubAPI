package com.itechart.finnhubapi.service;

import com.itechart.finnhubapi.dto.financialdto.FinancialStatementDto;
import com.itechart.finnhubapi.dto.metricdto.MetricDetailsDto;
import com.itechart.finnhubapi.dto.metricdto.MetricDto;
import com.itechart.finnhubapi.exceptions.FinancialException;
import com.itechart.finnhubapi.exceptions.MetricException;
import com.itechart.finnhubapi.feignservice.FinnhubClient;
import com.itechart.finnhubapi.model.Subscription;
import com.itechart.finnhubapi.model.entity.CompanyEntity;
import com.itechart.finnhubapi.model.entity.RoleEntity;
import com.itechart.finnhubapi.model.entity.SubscriptionEntity;
import com.itechart.finnhubapi.model.entity.SubscriptionTypeEntity;
import com.itechart.finnhubapi.model.entity.UserEntity;
import com.itechart.finnhubapi.service.impl.CompanyUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@ExtendWith(
        MockitoExtension.class
)
class StockServiceTest {
    @Mock
    private FinnhubClient serviceFeignClient;
    @Mock
    private UserService userService;
    @Mock
    private CompanyUserService companyUserService;
    @InjectMocks
    private StockService stockService;

    private final UserEntity user = new UserEntity();
    private final SubscriptionEntity subscription = new SubscriptionEntity();
    private final SubscriptionTypeEntity subscriptionType = new SubscriptionTypeEntity();
    private final RoleEntity role = new RoleEntity();
    private final CompanyEntity company = new CompanyEntity();
    private final FinancialStatementDto finance = new FinancialStatementDto();
    private final MetricDto metric = new MetricDto();

    @BeforeEach
    void setUp() {
        user.setId(3L);
        user.setEmail("test@gmail.com");
        user.setUsername("testUser");
        user.setPassword("test");
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());
        user.setStatus("ACTIVE");
        user.setFirstName("TestFirst");
        user.setLastName("TestLast");
        role.setName("ROLE_USER");
        List<RoleEntity> listRole = new ArrayList<>();
        listRole.add(role);
        user.setRoles(listRole);
        company.setSymbol("ZETA");
        company.setMic("OOTC");
        company.setType("Common Stock");
        company.setId(2L);
        company.setFigi("BBG000BJL537");
        company.setCurrency("USD");
        company.setDescription("JOHN WOOD GROUP PLC");
        company.setDisplaySymbol("ZETA");
        finance.setSymbol("Test");
        MetricDetailsDto metricDetailsDto = new MetricDetailsDto();
        metric.setMetricDetails(metricDetailsDto);
    }

    @Test
    void getFinancialResponseEntityWithLowSubscription() {
        subscriptionType.setName(Subscription.LOW.toString());
        subscription.setType(subscriptionType);
        subscription.setStartTime(LocalDateTime.now());
        subscription.setFinishTime(LocalDateTime.now().plusYears(3));
        user.setSubscription(subscription);
        doReturn(user).when(userService).findUserEntityById(user.getId());
        assertThrows(FinancialException.class,
                () -> stockService.getFinancialResponseEntity("ZETA", user.getId()));
        verify(userService, times(1)).findUserEntityById(user.getId());
    }

    @Test
    void getFinancialResponseEntityWithHighSubscription() {
        subscriptionType.setName(Subscription.HIGH.toString());
        subscription.setType(subscriptionType);
        subscription.setStartTime(LocalDateTime.now());
        subscription.setFinishTime(LocalDateTime.now().plusYears(3));
        user.setSubscription(subscription);
        doReturn(user).when(userService).findUserEntityById(user.getId());
        doReturn(true).when(companyUserService).isCompany(anyString(), anyLong());
        doReturn(finance).when(serviceFeignClient).getFinance(anyString(), eq(null));
        FinancialStatementDto response = stockService.getFinancialResponseEntity("ZETA", user.getId());
        assertThat(response).isNotNull();
        verify(userService, times(1)).findUserEntityById(user.getId());
        verify(serviceFeignClient, times(1)).getFinance(anyString(), eq(null));
    }

    @Test
    void getMetricResponseEntityWithLowSubscription() {
        subscriptionType.setName(Subscription.LOW.toString());
        subscription.setType(subscriptionType);
        subscription.setStartTime(LocalDateTime.now());
        subscription.setFinishTime(LocalDateTime.now().plusYears(3));
        user.setSubscription(subscription);
        doReturn(user).when(userService).findUserEntityById(user.getId());
        assertThrows(MetricException.class,
                () -> stockService.getMetricResponseEntity("ZETA", user.getId()));
        verify(userService, times(1)).findUserEntityById(user.getId());
    }

    @Test
    void getMetricResponseEntityWithMediumSubscription() {
        subscriptionType.setName(Subscription.MEDIUM.toString());
        subscription.setType(subscriptionType);
        subscription.setStartTime(LocalDateTime.now());
        subscription.setFinishTime(LocalDateTime.now().plusYears(3));
        user.setSubscription(subscription);
        doReturn(user).when(userService).findUserEntityById(user.getId());
        doReturn(true).when(companyUserService).isCompany(anyString(), anyLong());
        doReturn(metric).when(serviceFeignClient).getMetric(anyString(), eq(null));
        MetricDto response = stockService.getMetricResponseEntity("ZETA", user.getId());
        assertThat(response).isNotNull();
        verify(userService, times(1)).findUserEntityById(user.getId());
        verify(serviceFeignClient, times(1)).getMetric(anyString(), eq(null));
    }
}