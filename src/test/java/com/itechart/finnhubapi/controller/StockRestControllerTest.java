package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.financialdto.FinancialStatementDto;
import com.itechart.finnhubapi.dto.metricdto.MetricDetailsDto;
import com.itechart.finnhubapi.dto.metricdto.MetricDto;
import com.itechart.finnhubapi.feignservice.ServiceFeignClient;
import com.itechart.finnhubapi.model.CompanyEntity;
import com.itechart.finnhubapi.model.RoleEntity;
import com.itechart.finnhubapi.model.Subscription;
import com.itechart.finnhubapi.model.SubscriptionEntity;
import com.itechart.finnhubapi.model.UserEntity;
import com.itechart.finnhubapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@ExtendWith(
        MockitoExtension.class
)
class StockRestControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private ServiceFeignClient serviceFeignClient;
    @InjectMocks
    private StockRestController stockRestController;

    @Value(value = "${feign.token}")
    private String token;

    protected MockMvc mvc;
    private final UserEntity user = new UserEntity();
    private final SubscriptionEntity subscription = new SubscriptionEntity();
    private final RoleEntity role = new RoleEntity();
    private final CompanyEntity company = new CompanyEntity();

    @BeforeEach
    void setUp() {
        this.mvc = MockMvcBuilders.standaloneSetup(stockRestController).build();
        user.setEmail("test@gmail.com");
        user.setUsername("testUser");
        user.setPassword("test");
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());
        user.setStatus("ACTIVE");
        user.setFirstName("TestFirst");
        user.setLastName("TestLast");
        subscription.setName(Subscription.LOW.toString());
        subscription.setStartTime(LocalDateTime.now());
        subscription.setFinishTime(LocalDateTime.now().plusYears(3));
        user.setSubscription(subscription);
        role.setName("ROLE_USER");
        List<RoleEntity> listRole = new ArrayList<>();
        listRole.add(role);
        user.setRoles(listRole);
        company.setSymbol("Test");
        company.setMic("OOTC");
        company.setType("Common Stock");
        company.setId(2L);
        company.setFigi("BBG000BJL537");
        company.setCurrency("USD");
        company.setDescription("JOHN WOOD GROUP PLC");
        company.setDisplaySymbol("Test");
        List<CompanyEntity> companyEntities = new ArrayList<>();
        companyEntities.add(company);
        user.setCompanies(companyEntities);
    }

    @Test
    void financialsWithLowSubscription() throws Exception {
        doReturn(user).when(userService).findByUsername(null);
        MvcResult result = mvc.perform(get("/api/v1/stock/financials/{symbol}", "Test"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(userService, times(1)).findByUsername(null);
    }

    @Test
    void financialsWithHighSubscription() throws Exception {
        subscription.setName(Subscription.HIGH.toString());
        user.setSubscription(subscription);
        FinancialStatementDto finance = new FinancialStatementDto();
        finance.setSymbol("Test");
        doReturn(user).when(userService).findByUsername(null);
        doReturn(finance).when(serviceFeignClient).getFinance(anyString(), eq(token));
        MvcResult result = mvc.perform(get("/api/v1/stock/financials/{symbol}", "Test"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(userService, times(1)).findByUsername(null);
        verify(serviceFeignClient, times(1)).getFinance(anyString(), eq(token));
    }

    @Test
    void metricWithLowSubscription() throws Exception {
        doReturn(user).when(userService).findByUsername(null);
        MvcResult result = mvc.perform(get("/api/v1/stock/metric/{symbol}", "Test"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(userService, times(1)).findByUsername(null);
    }

    @Test
    void metricWithMediumSubscription() throws Exception {
        subscription.setName(Subscription.MEDIUM.toString());
        user.setSubscription(subscription);
        MetricDto metric = new MetricDto();
        MetricDetailsDto metricDetailsDto = new MetricDetailsDto();
        metric.setMetricDetails(metricDetailsDto);
        doReturn(user).when(userService).findByUsername(null);
        doReturn(metric).when(serviceFeignClient).getMetric(anyString(), eq(token));
        MvcResult result = mvc.perform(get("/api/v1/stock/metric/{symbol}", "Test"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(userService, times(1)).findByUsername(null);
        verify(serviceFeignClient, times(1)).getMetric(anyString(), eq(token));
    }
}