package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.CompanyDtoRequest;
import com.itechart.finnhubapi.mapper.CompanyMapper;
import com.itechart.finnhubapi.model.entity.CompanyEntity;
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

import static com.itechart.finnhubapi.controller.MainControllerTest.asJsonString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        MvcResult result = mvc.perform(get("/api/v1/company/getOneCompany/{symbol}", "TSLA"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(companyService, times(1)).getBySymbol(anyString());
    }

    @Test
    void saveCompanyToDB() throws Exception {
        List<CompanyDto> companyDtos = new ArrayList<>();
        CompanyDto companyDto = CompanyMapper.INSTANCE.companyToCompanyDto(company);
        companyDtos.add(companyDto);
        doReturn(companyDtos).when(companyService).getAllCompanyFromFeign();
        doReturn(true).when(companyService).save(anyList());
        MvcResult result = mvc.perform(post("/api/v1/company/saveAllCompanies"))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(companyService, times(1)).getAllCompanyFromFeign();
        verify(companyService, times(1)).save(anyList());
    }

    @Test
    void dontSaveCompanyToDB() throws Exception {
        List<CompanyDto> companyDtos = new ArrayList<>();
        CompanyDto companyDto = CompanyMapper.INSTANCE.companyToCompanyDto(company);
        companyDtos.add(companyDto);
        doReturn(companyDtos).when(companyService).getAllCompanyFromFeign();
        doReturn(false).when(companyService).save(anyList());
        MvcResult result = mvc.perform(post("/api/v1/company/saveAllCompanies"))
                .andExpect(status().isNotModified())
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(companyService, times(1)).getAllCompanyFromFeign();
        verify(companyService, times(1)).save(anyList());
    }

    @Test
    void saveQuoteToDB() throws Exception {
        doReturn(true).when(companyService).saveQuote();
        MvcResult result = mvc.perform(post("/api/v1/company/saveQuotes"))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(companyService, times(1)).saveQuote();
    }

    @Test
    void dontSaveQuoteToDB() throws Exception {
        doReturn(false).when(companyService).saveQuote();
        MvcResult result = mvc.perform(post("/api/v1/company/saveQuotes"))
                .andExpect(status().isNotModified())
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(companyService, times(1)).saveQuote();
    }

    @Test
    void deleteCompany() throws Exception {
        CompanyDtoRequest companyDtoRequest = CompanyMapper.INSTANCE.companyToCompanyDtoRequest(company);
        doReturn(true).when(companyService).deleteCompany(anyString());
        MvcResult result = mvc.perform(post("/api/v1/company/deleteCompany")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(companyDtoRequest)))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(companyService, times(1)).deleteCompany(anyString());
    }

    @Test
    void dontDeleteCompany() throws Exception {
        CompanyDtoRequest companyDtoRequest = CompanyMapper.INSTANCE.companyToCompanyDtoRequest(company);
        doReturn(false).when(companyService).deleteCompany(anyString());
        MvcResult result = mvc.perform(post("/api/v1/company/deleteCompany")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(companyDtoRequest)))
                .andExpect(status().isNotModified())
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(companyService, times(1)).deleteCompany(anyString());
    }
}