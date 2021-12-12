package com.itechart.finnhubapi.service;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.feignservice.QuoteMicroserviceClient;
import com.itechart.finnhubapi.feignservice.FinnhubClient;
import com.itechart.finnhubapi.mapper.CompanyMapper;
import com.itechart.finnhubapi.model.entity.CompanyEntity;
import com.itechart.finnhubapi.repository.CompanyRepository;
import com.itechart.finnhubapi.service.impl.CompanyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    private FinnhubClient serviceFeignClient;
    @Mock
    private QuoteMicroserviceClient microserviceFeignClient;
    @InjectMocks
    private CompanyServiceImpl companyService;

    private final CompanyEntity company = new CompanyEntity();

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
    }

    @Test
    void getEntityBySymbol() {
        doReturn(Optional.of(company)).when(companyRepository).findById(company.getId());
        CompanyEntity companyEntity = companyService.getEntityById(company.getId());
        assertThat(companyEntity).isEqualTo(company);
        verify(companyRepository, times(1)).findById(company.getId());
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
        List<CompanyDto> companyEntityList = companyService.findAll();
        assertThat(companyEntityList.get(0).getSymbol()).isEqualTo(companyEntities.get(0).getSymbol());
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
        doReturn(Optional.of(company)).when(companyRepository).findById(anyLong());
        doNothing().when(companyRepository).deleteById(anyLong());
        doNothing().when(microserviceFeignClient).deleteCompany(anyString());
        doReturn(false).when(companyRepository).existsById(anyLong());
        boolean isDeleteCompany = companyService.deleteCompany(company.getId());
        assertThat(isDeleteCompany).isTrue();
        verify(companyRepository, times(1)).findById(anyLong());
        verify(companyRepository, times(1)).deleteById(anyLong());
        verify(companyRepository, times(1)).existsById(anyLong());
    }
}