package com.itechart.finnhubapi.service.impl;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.UserDto;
import com.itechart.finnhubapi.dto.UserDtoResponse;
import com.itechart.finnhubapi.exceptions.*;
import com.itechart.finnhubapi.mapper.CompanyMapper;
import com.itechart.finnhubapi.mapper.UserMapper;
import com.itechart.finnhubapi.model.CompanyEntity;
import com.itechart.finnhubapi.model.Role;
import com.itechart.finnhubapi.model.RoleEntity;
import com.itechart.finnhubapi.model.Status;
import com.itechart.finnhubapi.model.Subscription;
import com.itechart.finnhubapi.model.SubscriptionEntity;
import com.itechart.finnhubapi.model.UserEntity;
import com.itechart.finnhubapi.repository.CompanyRepository;
import com.itechart.finnhubapi.repository.RoleRepository;
import com.itechart.finnhubapi.repository.SubscriptionRepository;
import com.itechart.finnhubapi.repository.UserRepository;
import com.itechart.finnhubapi.service.UserService;
import com.itechart.finnhubapi.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender emailSender;

    public UserEntity findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public void deleteById(long id) {
        findById(id);
        userRepository.deleteById(id);
    }

    public UserDtoResponse saveUser(UserDto user) {
        if (isEmailOrLoginInDataBase(user.getEmail(), user.getUsername())) {
            throw new EmailOrLoginInDataBaseException(user.getEmail(), user.getUsername());
        }
        UserEntity userEntity = UserMapper.INSTANCE.userDtoToUserEntity(user);
        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setName(Subscription.INACTIVE.toString());
        SubscriptionEntity saveSubscription = subscriptionRepository.save(subscription);
        userEntity.setSubscription(saveSubscription);
        RoleEntity role = roleRepository.findByName(Role.ROLE_USER_INACTIVE.toString());
        List<RoleEntity> userRoles = new ArrayList<>();
        userRoles.add(role);
        userEntity.setStatus(Status.ACTIVE.toString());
        userEntity.setRoles(userRoles);
        userEntity.setCreated(LocalDateTime.now());
        userEntity.setUpdated(LocalDateTime.now());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        UserDtoResponse savedUser = UserMapper.INSTANCE.userToUserDtoResponse(userRepository.save(userEntity));
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(savedUser.getEmail());
        message.setSubject("FinnhubAPI");
        message.setText("Hello!\n" +
                "You have successfully registered on the FinnhubAPI \n" +
                "Your login: " + user.getUsername() + "\n" +
                "Your password: " + user.getPassword() + "\n" +
                "to activate the subscription, follow the link: " + "http://localhost:8080/api/v1/subscription/payment");
        emailSender.send(message);
        return savedUser;
    }

    public UserDtoResponse updateUser(UserDto user) {
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
        return UserMapper.INSTANCE.userToUserDtoResponse(userRepository.save(userEntity));
    }

    public List<UserEntity> findAll() {
        List<UserEntity> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new NoDataFoundException();
        }
        return users;
    }

    public UserEntity addCompany(String symbol) {
        UserEntity user = findByUsername(UserUtil.userName());
        if (UserUtil.isCompany(symbol, user)) {
            throw new CompanyAlreadyOnListException(symbol);
        }
        SubscriptionEntity subscription = user.getSubscription();
        CompanyEntity company = companyRepository.findBySymbol(symbol).orElseThrow(
                () -> new CompanyNotFoundException(symbol));
        List<CompanyEntity> companies = user.getCompanies();
        if (Subscription.LOW.toString().equals(subscription.getName())) {
            if (companies.size() >= 2) {
                companies.remove(0);
            }
            companies.add(company);
        } else if (Subscription.MEDIUM.toString().equals(subscription.getName()) ||
                Subscription.HIGH.toString().equals(subscription.getName())) {
            if (companies.size() >= 3) {
                companies.remove(0);
            }
            companies.add(company);
        }
        user.setCompanies(companies);
        return userRepository.save(user);
    }

    public UserEntity lockOrUnlock(Long id, String status) {
        UserEntity user = findById(id);
        user.setStatus(status);
        UserEntity userEntity = userRepository.save(user);
        if (status.equals(Status.ACTIVE.toString())) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(userEntity.getEmail());
            message.setSubject("FinnhubAPI");
            message.setText("you were unblocked");
            emailSender.send(message);
        } else {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(userEntity.getEmail());
            message.setSubject("FinnhubAPI");
            message.setText("you were blocked");
            emailSender.send(message);
        }
        return userEntity;
    }

    public List<CompanyDto> getCompaniesFromUser(String username) {
        List<CompanyEntity> companies = findByUsername(username).getCompanies();
        if (companies.isEmpty()) {
            throw new EmptyListCompanyException();
        }
        return companies
                .stream()
                .map(CompanyMapper.INSTANCE::companyToCompanyDto)
                .collect(Collectors.toList());
    }

    public UserEntity findByUsername(String userName) {
        return userRepository.findByUsername(userName).orElseThrow(() ->
                new UserNotFoundException(userName));
    }

    public UserEntity changeSubscription(Subscription subscription) {
        UserEntity user = findByUsername(UserUtil.userName());
        SubscriptionEntity subscriptionEntity = user.getSubscription();
        if (subscription.equals(Subscription.INACTIVE)) {
            RoleEntity role = roleRepository.findByName(Role.ROLE_USER_INACTIVE.toString());
            List<RoleEntity> userRoles = new ArrayList<>();
            userRoles.add(role);
            user.setRoles(userRoles);
            subscriptionEntity.setName(subscription.toString());
            subscriptionEntity.setStartTime(null);
            subscriptionEntity.setFinishTime(null);
            SubscriptionEntity saveSubscription = subscriptionRepository.save(subscriptionEntity);
            user.setSubscription(saveSubscription);
        } else {
            if (!user.getRoles().get(0).getName().equals(Role.ROLE_ADMIN.toString())) {
                RoleEntity role = roleRepository.findByName(Role.ROLE_USER.toString());
                List<RoleEntity> userRoles = new ArrayList<>();
                userRoles.add(role);
                user.setRoles(userRoles);
            }
            subscriptionEntity.setName(subscription.toString());
            subscriptionEntity.setStartTime(LocalDateTime.now());
            subscriptionEntity.setFinishTime(LocalDateTime.now().plusMonths(3));
            SubscriptionEntity saveSubscription = subscriptionRepository.save(subscriptionEntity);
            if (subscription.equals(Subscription.LOW) && user.getCompanies().size() > 2) {
                List<CompanyEntity> companies = user.getCompanies();
                companies.remove(companies.size() - 1);
                user.setCompanies(companies);
            }
            user.setSubscription(saveSubscription);
        }
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
                () -> new CompanyNotFoundException(symbol));
        if (companies.contains(company)) {
            companies.remove(company);
        }else {
            throw new CompanyIsNotOnListException(symbol);
        }
        user.setCompanies(companies);
        UserEntity userEntity = userRepository.save(user);
        return userEntity.getCompanies()
                .stream()
                .map(CompanyMapper.INSTANCE::companyToCompanyDto)
                .collect(Collectors.toList());
    }

    private boolean isEmailOrLoginInDataBase(String email, String login) {
        UserEntity userEntityByEmail = userRepository.findByEmail(email).orElse(null);
        UserEntity userEntityByLogin = userRepository.findByUsername(login).orElse(null);
        return userEntityByLogin != null && userEntityByEmail != null;
    }
}