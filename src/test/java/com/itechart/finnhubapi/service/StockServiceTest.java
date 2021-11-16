package com.itechart.finnhubapi.service;

import com.itechart.finnhubapi.dto.financialdto.FinancialStatementDto;
import com.itechart.finnhubapi.dto.metricdto.MetricDetailsDto;
import com.itechart.finnhubapi.dto.metricdto.MetricDto;
import com.itechart.finnhubapi.exceptions.FinancialException;
import com.itechart.finnhubapi.exceptions.MetricException;
import com.itechart.finnhubapi.feignservice.ServiceFeignClient;
import com.itechart.finnhubapi.model.CompanyEntity;
import com.itechart.finnhubapi.model.RoleEntity;
import com.itechart.finnhubapi.model.Subscription;
import com.itechart.finnhubapi.model.SubscriptionEntity;
import com.itechart.finnhubapi.model.UserEntity;
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
    private ServiceFeignClient serviceFeignClient;
    @Mock
    private UserService userService;
    @InjectMocks
    private StockService stockService;

    private final UserEntity user = new UserEntity();
    private final SubscriptionEntity subscription = new SubscriptionEntity();
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
        company.setSymbol("WDGJF");
        company.setMic("OOTC");
        company.setType("Common Stock");
        company.setId(2L);
        company.setFigi("BBG000BJL537");
        company.setCurrency("USD");
        company.setDescription("JOHN WOOD GROUP PLC");
        company.setDisplaySymbol("WDGJF");
        List<CompanyEntity> companyEntities = new ArrayList<>();
        companyEntities.add(company);
        user.setCompanies(companyEntities);
        finance.setSymbol("Test");
        MetricDetailsDto metricDetailsDto = new MetricDetailsDto();
        metric.setMetricDetails(metricDetailsDto);
    }

    @Test
    void getFinancialResponseEntityWithLowSubscription() {
        subscription.setName(Subscription.LOW.toString());
        subscription.setStartTime(LocalDateTime.now());
        subscription.setFinishTime(LocalDateTime.now().plusYears(3));
        user.setSubscription(subscription);
        doReturn(user).when(userService).findByUsername(null);
        assertThrows(FinancialException.class,
                () -> stockService.getFinancialResponseEntity("WDGJF"));
        verify(userService, times(1)).findByUsername(null);
    }

    @Test
    void getFinancialResponseEntityWithHighSubscription() {
        subscription.setName(Subscription.HIGH.toString());
        subscription.setStartTime(LocalDateTime.now());
        subscription.setFinishTime(LocalDateTime.now().plusYears(3));
        user.setSubscription(subscription);
        doReturn(user).when(userService).findByUsername(null);
        doReturn(finance).when(serviceFeignClient).getFinance(anyString(), eq(null));
        FinancialStatementDto response = stockService.getFinancialResponseEntity("WDGJF");
        assertThat(response).isNotNull();
        verify(userService, times(1)).findByUsername(null);
        verify(serviceFeignClient, times(1)).getFinance(anyString(), eq(null));
    }

    @Test
    void getMetricResponseEntityWithLowSubscription() {
        subscription.setName(Subscription.LOW.toString());
        subscription.setStartTime(LocalDateTime.now());
        subscription.setFinishTime(LocalDateTime.now().plusYears(3));
        user.setSubscription(subscription);
        doReturn(user).when(userService).findByUsername(null);
        assertThrows(MetricException.class,
                () -> stockService.getMetricResponseEntity("WDGJF"));
        verify(userService, times(1)).findByUsername(null);
    }

    @Test
    void getMetricResponseEntityWithMediumSubscription() {
        subscription.setName(Subscription.MEDIUM.toString());
        subscription.setStartTime(LocalDateTime.now());
        subscription.setFinishTime(LocalDateTime.now().plusYears(3));
        user.setSubscription(subscription);
        doReturn(user).when(userService).findByUsername(null);
        doReturn(metric).when(serviceFeignClient).getMetric(anyString(), eq(null));
        MetricDto response = stockService.getMetricResponseEntity("WDGJF");
        assertThat(response).isNotNull();
        verify(userService, times(1)).findByUsername(null);
        verify(serviceFeignClient, times(1)).getMetric(anyString(), eq(null));
    }
}