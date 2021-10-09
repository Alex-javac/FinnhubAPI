package com.itechart.finnhubapi.service;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.UserDto;
import com.itechart.finnhubapi.exceptions.UserNotFoundException;
import com.itechart.finnhubapi.mapper.CompanyMapper;
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
import com.itechart.finnhubapi.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;


    public UserEntity findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    public UserEntity saveUser(UserDto user) {
        UserEntity userEntity = UserMapper.INSTANCE.userDtoToUserEntity(user);
        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setName(Subscription.LOW.toString());
        subscription.setStartTime(LocalDateTime.now());
        subscription.setFinishTime(LocalDateTime.now().plusYears(3));
        SubscriptionEntity saveSubscription = subscriptionRepository.save(subscription);
        userEntity.setSubscription(saveSubscription);
        RoleEntity role = roleRepository.findByName("ROLE_USER");
        List<RoleEntity> userRoles = new ArrayList<>();
        userRoles.add(role);
        userEntity.setStatus("ACTIVE");
        userEntity.setRoles(userRoles);
        userEntity.setCreated(LocalDateTime.now());
        userEntity.setUpdated(LocalDateTime.now());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(userEntity);
    }

    public UserEntity updateUser(UserDto user) {
        UserEntity userEntity = findByUsername(UserUtil.userName());
        if (user.getFirstName() != null) {
            userEntity.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            userEntity.setLastName(user.getLastName());
        }
        if (user.getUsername() != null) {
            userEntity.setUsername(user.getUsername());
        }
        if (user.getEmail() != null) {
            userEntity.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userEntity.setUpdated(LocalDateTime.now());
        return userRepository.save(userEntity);
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public UserEntity addCompany(String symbol) {
        UserEntity user = findByUsername(UserUtil.userName());
        SubscriptionEntity subscription = user.getSubscription();
        CompanyEntity company = companyRepository.findBySymbol(symbol).orElseThrow(
                () -> new RuntimeException(String.format("company named %s was not found", symbol)));
        List<CompanyEntity> companies = user.getCompanies();
        if ("LOW".equals(subscription.getName())) {
            if (companies.size() < 2) {
                companies.add(company);
            } else {
                companies.set(0, company);
            }
        } else if ("MEDIUM".equals(subscription.getName()) || "HIGH".equals(subscription.getName())) {
            if (companies.size() < 3) {
                companies.add(company);
            } else {
                companies.set(0, company);
            }
        }
        user.setCompanies(companies);
        return userRepository.save(user);
    }

    public UserEntity lockOrUnlock(Long id, String status) {
        UserEntity user = userRepository.getById(id);
        user.setStatus(status);
        return userRepository.save(user);
    }

    public List<CompanyDto> getCompaniesFromUser(String username) {
        List<CompanyEntity> companies = findByUsername(username).getCompanies();
        return companies
                .stream()
                .map(CompanyMapper.INSTANCE::companyToCompanyDto)
                .collect(Collectors.toList());
    }

    public UserEntity findByUsername(String userName) {
        return userRepository.findByUsername(userName).orElseThrow(() ->
                new UsernameNotFoundException("User doesn't exists"));
    }

    public UserEntity changeSubscription(Subscription subscription) {
        UserEntity user = findByUsername(UserUtil.userName());
        SubscriptionEntity subscriptionEntity = user.getSubscription();
        subscriptionEntity.setName(subscription.toString());
        subscriptionEntity.setStartTime(LocalDateTime.now());
        subscriptionEntity.setFinishTime(LocalDateTime.now().plusMonths(3));
        SubscriptionEntity saveSubscription = subscriptionRepository.save(subscriptionEntity);
        if(subscription.toString().equals("LOW")){
            List<CompanyEntity> companies = user.getCompanies();
            companies.remove(companies.size()-1);
            user.setCompanies(companies);
        }
        user.setSubscription(saveSubscription);
        return userRepository.save(user);
    }

    public UserEntity renewSubscription(long month) {
        UserEntity user = findByUsername(UserUtil.userName());
        SubscriptionEntity subscription = user.getSubscription();
        LocalDateTime plusMonths = subscription.getFinishTime().plusMonths(month);
        subscription.setFinishTime(plusMonths);
        SubscriptionEntity renewSubscription = subscriptionRepository.save(subscription);
        user.setSubscription(renewSubscription);
        return userRepository.save(user);
    }

    public List<CompanyDto> deleteOneCompanyFromUser(String symbol) {
        UserEntity user = findByUsername(UserUtil.userName());
        List<CompanyEntity> companies = user.getCompanies();
        CompanyEntity company = companyRepository.findBySymbol(symbol).orElseThrow(
                () -> new RuntimeException(String.format("company named %s was not found", symbol)));
        companies.remove(company);
        user.setCompanies(companies);
        UserEntity userEntity = userRepository.save(user);
       return userEntity.getCompanies()
                .stream()
                .map(CompanyMapper.INSTANCE::companyToCompanyDto)
                .collect(Collectors.toList());
    }
}