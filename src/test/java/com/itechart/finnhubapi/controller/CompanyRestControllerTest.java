package com.itechart.finnhubapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.mapper.CompanyMapper;
import com.itechart.finnhubapi.model.CompanyEntity;
import com.itechart.finnhubapi.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestMethodOrder(MethodOrderer.DisplayName.class)
@ExtendWith(
        MockitoExtension.class
)
class CompanyRestControllerTest {
    @Mock
    private CompanyService companyService;
    @InjectMocks
    private CompanyRestController companyRestController;

    protected MockMvc mvc;

    private final CompanyEntity company = new CompanyEntity();

    @BeforeEach
    void setUp() {
        this.mvc = MockMvcBuilders.standaloneSetup(companyRestController).build();
        company.setSymbol("Test");
        company.setMic("OOTC");
        company.setType("Common Stock");
        company.setId(2L);
        company.setFigi("BBG000BJL537");
        company.setCurrency("USD");
        company.setDescription("JOHN WOOD GROUP PLC");
        company.setDisplaySymbol("Test");
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllCompanies() throws Exception {
        List<CompanyEntity> companyEntities = new ArrayList<>();
        companyEntities.add(company);
        doReturn(companyEntities).when(companyService).findAll();
        MvcResult result = mvc.perform(get("/api/v1/company/getAllCompanies", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(companyService, times(1)).findAll();

    }

    @Test
    void getOneCompany() throws Exception {
        CompanyDto companyDto = CompanyMapper.INSTANCE.companyToCompanyDto(company);
        doReturn(companyDto).when(companyService).getBySymbol(anyString());
        MvcResult result = mvc.perform(get("/api/v1/company/getOneCompany/{symbol}","TSLA"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(companyService, times(1)).getBySymbol(anyString());

    }

    @Test
    void saveCompanyToDB() {
    }

    @Test
    void saveQuoteToDB() {
    }

    @Test
    void deleteCompany() {
    }
}