package com.itechart.finnhubapi.service;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.UserDto;
import com.itechart.finnhubapi.exceptions.UserNotFoundException;
import com.itechart.finnhubapi.mapper.CompanyMapper;
import com.itechart.finnhubapi.mapper.UserMapper;
import com.itechart.finnhubapi.model.CompanyEntity;
import com.itechart.finnhubapi.model.RoleEntity;
import com.itechart.finnhubapi.model.SubscriptionEntity;
import com.itechart.finnhubapi.model.UserEntity;
import com.itechart.finnhubapi.repository.RoleRepository;
import com.itechart.finnhubapi.repository.SubscriptionRepository;
import com.itechart.finnhubapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final CompanyService companyService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, SubscriptionRepository subscriptionRepository, CompanyService companyService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.companyService = companyService;
        this.passwordEncoder = passwordEncoder;
    }


    public UserEntity findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    public UserEntity saveUser(UserDto user) {
        UserEntity userEntity = UserMapper.INSTANCE.userDtoToUserEntity(user);
        SubscriptionEntity subscription = subscriptionRepository.findByName("LOW").orElse(null);
        userEntity.setSubscription(subscription);
        RoleEntity role = roleRepository.findByName("ROLE_USER");
        List<RoleEntity> userRoles = new ArrayList<>();
        userRoles.add(role);
        userEntity.setStatus("ACTIVE");
        userEntity.setRoles(userRoles);
        userEntity.setCreated(LocalDateTime.now());
        userEntity.setUpdated(LocalDateTime.now());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        UserEntity registeredUser = userRepository.save(userEntity);
        return registeredUser;
    }

    public UserEntity updateUser(UserDto user) {
        UserEntity userEntity = userRepository.getById(user.getId());
        if(user.getFirstName()!=null){userEntity.setFirstName(user.getFirstName());}
        if(user.getLastName()!=null){userEntity.setLastName(user.getLastName());}
        if(user.getUsername()!=null){userEntity.setUsername(user.getUsername());}
        if(user.getEmail()!=null) {userEntity.setEmail(user.getEmail());}
        if(user.getPassword()!=null){userEntity.setPassword(passwordEncoder.encode(user.getPassword()));}
        userEntity.setUpdated(LocalDateTime.now());
        return userRepository.save(userEntity);
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public UserEntity addCompany(Long id, List<String> companyDto) {
        UserEntity user = userRepository.getById(id);
        List<CompanyEntity> companyEntities = companyDto
                .stream()
                .map(companyService::getEntityBySymbol)
                .collect(Collectors.toList());
        user.setCompanies(companyEntities);
        return userRepository.save(user);
    }

    public UserEntity lockOrUnlock(Long id, String status) {
        UserEntity user = userRepository.getById(id);
        user.setStatus(status);
        return userRepository.save(user);
    }

    public List<CompanyDto> getCompaniesFromUser(Long id) {
        List<CompanyEntity> companies = findById(id).getCompanies();
        return companies
                .stream()
                .map(CompanyMapper.INSTANCE::companyToCompanyDto)
                .collect(Collectors.toList());
    }
}