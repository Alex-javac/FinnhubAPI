package com.itechart.finnhubapi.service;


import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.mapper.UserMapper;
import com.itechart.finnhubapi.model.CompanyEntity;
import com.itechart.finnhubapi.model.RoleEntity;
import com.itechart.finnhubapi.model.Subscription;
import com.itechart.finnhubapi.model.SubscriptionEntity;
import com.itechart.finnhubapi.model.UserEntity;
import com.itechart.finnhubapi.repository.CompanyRepository;
import com.itechart.finnhubapi.repository.RoleRepository;
import com.itechart.finnhubapi.repository.SubscriptionRepository;
import com.itechart.finnhubapi.repository.UserRepository;
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
import static org.mockito.Mockito.*;


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

    @InjectMocks
    private UserService userService;

    private final UserEntity user = new UserEntity();
    private final SubscriptionEntity subscription = new SubscriptionEntity();
    private final RoleEntity role = new RoleEntity();
    private final CompanyEntity company = new CompanyEntity();

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
        subscription.setName(Subscription.LOW.toString());
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
        user.setCompanies(new ArrayList<>());
    }


    @Test
    void findById() {
        doReturn(Optional.of(user)).when(userRepository).findById(3L);
        UserEntity userEntity = userService.findById(3L);
        assertThat(userEntity).isEqualTo(user);
    }

    @Test
    void throwExceptionIfDatabaseIsNotAvailable() {
        doThrow(RuntimeException.class).when(userRepository).deleteById(user.getId());
        assertThrows(RuntimeException.class, () -> userService.deleteById(user.getId()));
    }

    @Test
    void deleteById() {
        doNothing().when(userRepository).deleteById(user.getId());
        userService.deleteById(user.getId());
        verify(userRepository, times(1)).deleteById(anyLong());
    }


    @Test
    void saveUser() {
        doReturn(user).when(userRepository).save(any(UserEntity.class));
        UserEntity userEntity = userService.saveUser(UserMapper.INSTANCE.userToUserDto(user));
        assertThat(userEntity).isEqualTo(user);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void updateUser() {
        doReturn(user).when(userRepository).save(any(UserEntity.class));
        doReturn(Optional.of(user)).when(userRepository).findByUsername(any());
        UserEntity userEntity = userService.updateUser(UserMapper.INSTANCE.userToUserDto(user));
        assertThat(userEntity).isEqualTo(user);
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(userRepository, times(1)).findByUsername(any());
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
        doReturn(Optional.of(user)).when(userRepository).findByUsername(any());
        doReturn(Optional.of(company)).when(companyRepository).findBySymbol(any());
        doReturn(user).when(userRepository).save(any(UserEntity.class));
        UserEntity userEntity = userService.addCompany(company.getSymbol());
        assertThat(userEntity).isEqualTo(user);
        assertThat(userEntity.getCompanies()).isNotEmpty();
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(userRepository, times(1)).findByUsername(any());
        verify(companyRepository, times(1)).findBySymbol(any());
    }


    @Test
    void lockOrUnlock() {
        doReturn(user).when(userRepository).getById(any());
        doReturn(user).when(userRepository).save(any(UserEntity.class));
        UserEntity userEntity = userService.lockOrUnlock(user.getId(), "BLOCKED");
        assertThat(userEntity).isEqualTo(user);
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(userRepository, times(1)).getById(any());
    }


    @Test
    void findByUsername() {
        doReturn(Optional.of(user)).when(userRepository).findByUsername(user.getUsername());
        UserEntity userEntity = userService.findByUsername(user.getUsername());
        assertThat(userEntity).isEqualTo(user);
        verify(userRepository, times(1)).findByUsername(any());
    }


    @Test
    void getCompaniesFromUser() {
        List<CompanyEntity> companyEntities = new ArrayList<>();
        companyEntities.add(company);
        user.setCompanies(companyEntities);
        doReturn(Optional.of(user)).when(userRepository).findByUsername(user.getUsername());
        List<CompanyDto> companiesFromUser = userService.getCompaniesFromUser(user.getUsername());
        assertThat(companiesFromUser).isNotNull();
        verify(userRepository, times(1)).findByUsername(user.getUsername());
    }


    @Test
    void changeSubscription() {
        doReturn(Optional.of(user)).when(userRepository).findByUsername(any());
        doReturn(user.getSubscription()).when(subscriptionRepository).save(any(SubscriptionEntity.class));
        doReturn(user).when(userRepository).save(any(UserEntity.class));
        UserEntity userEntity = userService.changeSubscription(Subscription.HIGH);
        assertThat(userEntity.getSubscription()).isEqualTo(user.getSubscription());
        verify(userRepository, times(1)).findByUsername(any());
        verify(subscriptionRepository, times(1)).save(any(SubscriptionEntity.class));
        verify(userRepository, times(1)).save(any(UserEntity.class));

    }

    @Test
    void renewSubscription() {
        doReturn(Optional.of(user)).when(userRepository).findByUsername(any());
        doReturn(user.getSubscription()).when(subscriptionRepository).save(any(SubscriptionEntity.class));
        doReturn(user).when(userRepository).save(any(UserEntity.class));
        UserEntity userEntity = userService.changeSubscription(Subscription.HIGH);
        assertThat(userEntity.getSubscription()).isEqualTo(user.getSubscription());
        verify(userRepository, times(1)).findByUsername(any());
        verify(subscriptionRepository, times(1)).save(any(SubscriptionEntity.class));
        verify(userRepository, times(1)).save(any(UserEntity.class));

    }

    @Test
    void deleteOneCompanyFromUser() {
        List<CompanyEntity> companyEntities = new ArrayList<>();
        companyEntities.add(company);
        user.setCompanies(companyEntities);
        doReturn(Optional.of(user)).when(userRepository).findByUsername(any());
        doReturn(Optional.of(company)).when(companyRepository).findBySymbol(any());
        doReturn(user).when(userRepository).save(any(UserEntity.class));
        List<CompanyDto> companiesFromUser = userService.deleteOneCompanyFromUser(company.getSymbol());
        assertThat(companiesFromUser.size()).isZero();
        verify(userRepository, times(1)).findByUsername(any());
        verify(companyRepository, times(1)).findBySymbol(any());
        verify(userRepository, times(1)).save(any(UserEntity.class));

    }

}
