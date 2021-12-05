package com.itechart.finnhubapi.service;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.SubscriptionTypeDto;
import com.itechart.finnhubapi.dto.UserDtoResponse;
import com.itechart.finnhubapi.mapper.CompanyMapper;
import com.itechart.finnhubapi.mapper.UserMapper;
import com.itechart.finnhubapi.model.Subscription;
import com.itechart.finnhubapi.model.entity.CompanyEntity;
import com.itechart.finnhubapi.model.entity.RoleEntity;
import com.itechart.finnhubapi.model.entity.SubscriptionEntity;
import com.itechart.finnhubapi.model.entity.SubscriptionTypeEntity;
import com.itechart.finnhubapi.model.entity.UserEntity;
import com.itechart.finnhubapi.repository.*;
import com.itechart.finnhubapi.service.impl.CompanyUserService;
import com.itechart.finnhubapi.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@ExtendWith(
        MockitoExtension.class
)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JavaMailSender emailSender;
    @Mock
    private SubscriptionTypeRepository typeRepository;
    @Mock
    private CompanyUserService companyUserService;

    @InjectMocks
    private UserServiceImpl userService;

    private final UserEntity user = new UserEntity();
    private final SubscriptionEntity subscription = new SubscriptionEntity();
    private final RoleEntity role = new RoleEntity();
    private final CompanyEntity company = new CompanyEntity();
    private final SubscriptionTypeEntity subscriptionType = new SubscriptionTypeEntity();

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
        subscriptionType.setName(Subscription.LOW.toString());
        subscription.setType(subscriptionType);
        subscription.setStartTime(LocalDateTime.now());
        subscription.setFinishTime(LocalDateTime.now().plusYears(3));
        user.setSubscription(subscription);
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
    }

    @Test
    void findById() {
        doReturn(Optional.of(user)).when(userRepository).findById(3L);
        UserDtoResponse userEntity = userService.findById(3L);
        assertThat(userEntity.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void throwExceptionIfDatabaseIsNotAvailable() {
        doReturn(Optional.of(user)).when(userRepository).findById(anyLong());
        doThrow(RuntimeException.class).when(userRepository).deleteById(user.getId());
        assertThrows(RuntimeException.class, () -> userService.deleteById(user.getId()));
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void deleteById() {
        doReturn(Optional.of(user)).when(userRepository).findById(anyLong());
        doNothing().when(userRepository).deleteById(user.getId());
        userService.deleteById(user.getId());
        verify(userRepository, times(1)).deleteById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void saveUser() {
        doReturn(user).when(userRepository).save(any(UserEntity.class));
        doReturn(Optional.of(user.getSubscription().getType())).when(typeRepository).findByName(anyString());
        UserDtoResponse userEntity = userService.saveUser(UserMapper.INSTANCE.userToUserDto(user));
        assertThat(userEntity.getEmail()).isEqualTo(user.getEmail());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void updateUser() {
        doReturn(user).when(userRepository).save(any(UserEntity.class));
        doReturn(Optional.of(user)).when(userRepository).findByUsername(any());
        doReturn(Optional.empty()).when(userRepository).findByEmail(anyString());
        doReturn(Optional.empty()).when(userRepository).findByUsername(anyString());
        UserDtoResponse userEntity = userService.updateUser(UserMapper.INSTANCE.userToUserUpdateDto(user));
        assertThat(userEntity.getEmail()).isEqualTo(user.getEmail());
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    void findAll() {
        List<UserEntity> userList = new ArrayList<>();
        userList.add(user);
        doReturn(userList).when(userRepository).findAll();
        List<UserEntity> entityList = userRepository.findAll();
        assertThat(entityList).isNotNull().isEqualTo(userList);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void addCompany() {
        List<CompanyDto> companyDtoList =new ArrayList<>();
        companyDtoList.add(CompanyMapper.INSTANCE.companyToCompanyDto(company));
        doReturn(Optional.of(user)).when(userRepository).findByUsername(null);
        doReturn(companyDtoList).when(companyUserService).addCompanyToUser(anyString(),anyLong(),any(SubscriptionTypeDto.class));
        List<CompanyDto> result = userService.addCompany(company.getSymbol());
        assertThat(result).isEqualTo(companyDtoList);
        verify(userRepository, times(1)).findByUsername(null);
        verify(companyUserService, times(1)).addCompanyToUser(anyString(),anyLong(),any(SubscriptionTypeDto.class));
    }

    @Test
    void lockOrUnlock() {
        doReturn(Optional.of(user)).when(userRepository).findById(anyLong());
        doReturn(user).when(userRepository).save(any(UserEntity.class));
        UserDtoResponse userEntity = userService.lockOrUnlock(user.getId(), "BLOCKED");
        assertThat(userEntity.getUsername()).isEqualTo(user.getUsername());
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void findByUsername() {
        doReturn(Optional.of(user)).when(userRepository).findByUsername(user.getUsername());
        UserEntity userEntity = userService.findByUsername(user.getUsername());
        assertThat(userEntity).isEqualTo(user);
        verify(userRepository, times(1)).findByUsername(any());
    }

    @Test
    void changeSubscription() {
        doReturn(Optional.of(user)).when(userRepository).findByUsername(any());
        doReturn(user.getSubscription()).when(subscriptionRepository).save(any(SubscriptionEntity.class));
        doReturn(Optional.of(user.getSubscription().getType())).when(typeRepository).findById(anyLong());
        doReturn(user).when(userRepository).save(any(UserEntity.class));
        UserDtoResponse userEntity = userService.changeSubscription(4L);
        assertThat(userEntity.getSubscription().getType().getName()).isEqualTo(user.getSubscription().getType().getName());
        verify(userRepository, times(1)).findByUsername(any());
        verify(subscriptionRepository, times(1)).save(any(SubscriptionEntity.class));
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(typeRepository, times(1)).findById(anyLong());
    }

    @Test
    void renewSubscription() {
        doReturn(Optional.of(user)).when(userRepository).findByUsername(any());
        doReturn(user.getSubscription()).when(subscriptionRepository).save(any(SubscriptionEntity.class));
        doReturn(Optional.of(user.getSubscription().getType())).when(typeRepository).findById(anyLong());
        doReturn(user).when(userRepository).save(any(UserEntity.class));
        UserDtoResponse userEntity = userService.changeSubscription(4L);
        assertThat(userEntity.getSubscription().getType().getName()).isEqualTo(user.getSubscription().getType().getName());
        verify(userRepository, times(1)).findByUsername(any());
        verify(subscriptionRepository, times(1)).save(any(SubscriptionEntity.class));
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void deleteOneCompanyFromUser() {
        List<CompanyDto> companyDto= new ArrayList<>();
        companyDto.add(CompanyMapper.INSTANCE.companyToCompanyDto(company));
        doReturn(Optional.of(user)).when(userRepository).findByUsername(any());
        doReturn(Optional.of(company)).when(companyRepository).findBySymbol(any());
        doReturn(companyDto).when(companyUserService).getCompaniesFromUser(anyLong());
        List<CompanyDto> companiesFromUser = userService.deleteOneCompanyFromUser(company.getSymbol());
        assertThat(companiesFromUser.size()).isNotNull();
        verify(userRepository, times(1)).findByUsername(any());
        verify(companyRepository, times(1)).findBySymbol(any());
        verify(companyUserService, times(2)).getCompaniesFromUser(anyLong());
    }
}