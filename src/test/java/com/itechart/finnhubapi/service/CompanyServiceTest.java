package com.itechart.finnhubapi.service;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.feignservice.MicroserviceFeignClient;
import com.itechart.finnhubapi.feignservice.ServiceFeignClient;
import com.itechart.finnhubapi.mapper.CompanyMapper;
import com.itechart.finnhubapi.mapper.QuoteMapper;
import com.itechart.finnhubapi.model.CompanyEntity;
import com.itechart.finnhubapi.model.QuoteEntity;
import com.itechart.finnhubapi.repository.CompanyRepository;
import com.itechart.finnhubapi.repository.QuoteRepository;
import com.itechart.finnhubapi.service.impl.CompanyServiceImpl;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@ExtendWith(
        MockitoExtension.class
)
class CompanyServiceTest {
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private QuoteRepository quoteRepository;
    @Mock
    private ServiceFeignClient serviceFeignClient;
    @Mock
    private MicroserviceFeignClient microserviceFeignClient;
    @InjectMocks
    private CompanyServiceImpl companyService;

    private final CompanyEntity company = new CompanyEntity();
    private final QuoteEntity quote = new QuoteEntity();

    @BeforeEach
    void setUp() {
        company.setSymbol("WDGJF");
        company.setMic("OOTC");
        company.setType("Common Stock");
        company.setId(2L);
        company.setFigi("BBG000BJL537");
        company.setCurrency("USD");
        company.setDescription("JOHN WOOD GROUP PLC");
        company.setDisplaySymbol("WDGJF");
        quote.setC(3.2);
        quote.setD(0.2199);
        quote.setDp(7.3789);
        quote.setH(3.2);
        quote.setL(3.2);
        quote.setO(3.2);
        quote.setPc(2.9801);
        quote.setT(1633708145);
        quote.setDate(LocalDateTime.now());
    }

    @Test
    void getEntityBySymbol() {
        doReturn(Optional.of(company)).when(companyRepository).findBySymbol(company.getSymbol());
        CompanyEntity companyEntity = companyService.getEntityBySymbol(company.getSymbol());
        assertThat(companyEntity).isEqualTo(company);
        verify(companyRepository, times(1)).findBySymbol(company.getSymbol());
    }

    @Test
    void getBySymbol() {
        doReturn(Optional.of(company)).when(companyRepository).findBySymbol(company.getSymbol());
        CompanyDto companyDto = companyService.getBySymbol(company.getSymbol());
        assertThat(companyDto).isExactlyInstanceOf(CompanyDto.class);
        verify(companyRepository, times(1)).findBySymbol(company.getSymbol());
    }

    @Test
    void save() {
        List<CompanyDto> companyDtos = new ArrayList<>();
        companyDtos.add(CompanyMapper.INSTANCE.companyToCompanyDto(company));
        doReturn(Optional.of(company)).when(companyRepository).findBySymbol(company.getSymbol());
        boolean save = companyService.save(companyDtos);
        assertThat(save).isTrue();
        verify(companyRepository, times(1)).findBySymbol(company.getSymbol());
    }

    @Test
    void getAllCompanyFromFeign() {
        List<CompanyDto> companyDtos = new ArrayList<>();
        companyDtos.add(CompanyMapper.INSTANCE.companyToCompanyDto(company));
        doReturn(companyDtos).when(serviceFeignClient).getCompany(null);
        List<CompanyDto> companyFromFeign = companyService.getAllCompanyFromFeign();
        assertThat(companyFromFeign).isEqualTo(companyDtos);
        verify(serviceFeignClient, times(1)).getCompany(null);
    }

    @Test
    void findAll() {
        List<CompanyEntity> companyEntities = new ArrayList<>();
        companyEntities.add(company);
        doReturn(companyEntities).when(companyRepository).findAll();
        List<CompanyEntity> companyEntityList = companyService.findAll();
        assertThat(companyEntityList).isEqualTo(companyEntities);
        verify(companyRepository, times(1)).findAll();
    }

    @Test
    void saveQuote() {
        doNothing().when(microserviceFeignClient).saveQuote();
        boolean saveQuote = companyService.saveQuote();
        assertThat(saveQuote).isTrue();
        verify(microserviceFeignClient, times(1)).saveQuote();
    }

    @Test
    void deleteCompany() {
        doReturn(Optional.of(company)).when(companyRepository).findBySymbol(company.getSymbol());
        doNothing().when(companyRepository).deleteById(anyLong());
        doNothing().when(microserviceFeignClient).deleteCompany(anyString());
        doReturn(false).when(companyRepository).existsById(anyLong());
        boolean isDeleteCompany = companyService.deleteCompany(company.getSymbol());
        assertThat(isDeleteCompany).isTrue();
        verify(companyRepository, times(1)).findBySymbol(company.getSymbol());
        verify(companyRepository, times(1)).deleteById(anyLong());
        verify(companyRepository, times(1)).existsById(anyLong());
    }
}