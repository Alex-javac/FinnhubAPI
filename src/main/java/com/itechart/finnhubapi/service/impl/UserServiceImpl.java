package com.itechart.finnhubapi.service.impl;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.SubscriptionTypeDto;
import com.itechart.finnhubapi.dto.UserDto;
import com.itechart.finnhubapi.dto.UserDtoResponse;
import com.itechart.finnhubapi.dto.UserUpdateDto;
import com.itechart.finnhubapi.exceptions.CheckValueException;
import com.itechart.finnhubapi.exceptions.CompanyIsNotOnListException;
import com.itechart.finnhubapi.exceptions.CompanyNotFoundException;
import com.itechart.finnhubapi.exceptions.EmailOrLoginInDataBaseException;
import com.itechart.finnhubapi.exceptions.IncorrectPasswordOrLoginException;
import com.itechart.finnhubapi.exceptions.NoDataFoundException;
import com.itechart.finnhubapi.exceptions.SubscriptionTypeException;
import com.itechart.finnhubapi.exceptions.UserNotFoundException;
import com.itechart.finnhubapi.mapper.CompanyMapper;
import com.itechart.finnhubapi.mapper.UserMapper;
import com.itechart.finnhubapi.model.Role;
import com.itechart.finnhubapi.model.StatusSubscription;
import com.itechart.finnhubapi.model.StatusUser;
import com.itechart.finnhubapi.model.Subscription;
import com.itechart.finnhubapi.model.entity.CompanyEntity;
import com.itechart.finnhubapi.model.entity.RoleEntity;
import com.itechart.finnhubapi.model.entity.SubscriptionEntity;
import com.itechart.finnhubapi.model.entity.SubscriptionTypeEntity;
import com.itechart.finnhubapi.model.entity.UserEntity;
import com.itechart.finnhubapi.repository.CompanyRepository;
import com.itechart.finnhubapi.repository.RoleRepository;
import com.itechart.finnhubapi.repository.SubscriptionRepository;
import com.itechart.finnhubapi.repository.SubscriptionTypeRepository;
import com.itechart.finnhubapi.repository.UserRepository;
import com.itechart.finnhubapi.service.UserService;
import com.itechart.finnhubapi.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender emailSender;
    private final SubscriptionTypeRepository typeRepository;
    private final CompanyUserService companyUserService;

    @Transactional
    @Override
    public UserDtoResponse findById(long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return UserMapper.INSTANCE.userToUserDtoResponse(userEntity);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        findById(id);
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public UserEntity findByLoginAndPassword(String userName, String password) {
        UserEntity userEntity = findByUsername(userName);
        if (userEntity != null) {
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                return userEntity;
            }
        }
        throw new IncorrectPasswordOrLoginException();
    }

    @Transactional
    @Override
    public UserDtoResponse saveUser(UserDto user) {
        if (isEmailOrLoginInDataBase(user.getEmail(), user.getUsername())) {
            throw new EmailOrLoginInDataBaseException(user.getEmail(), user.getUsername());
        }
        UserEntity userEntity = UserMapper.INSTANCE.userDtoToUserEntity(user);
        SubscriptionEntity subscription = new SubscriptionEntity();
        SubscriptionTypeEntity subscriptionType = typeRepository.findByName(Subscription.BASIC.toString()).orElseThrow(SubscriptionTypeException::new);
        subscription.setType(subscriptionType);
        subscription.setStartTime(LocalDateTime.now());
        subscription.setFinishTime(LocalDateTime.now().plusYears(1));
        subscription.setStatus(StatusSubscription.ACTIVE.toString());
        SubscriptionEntity saveSubscription = subscriptionRepository.save(subscription);
        userEntity.setSubscription(saveSubscription);
        RoleEntity role = roleRepository.findByName(Role.ROLE_USER_INACTIVE.toString());
        List<RoleEntity> userRoles = new ArrayList<>();
        userRoles.add(role);
        userEntity.setStatus(StatusUser.ACTIVE.toString());
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

    @Transactional
    @Override
    public UserDtoResponse updateUser(UserUpdateDto user) {
        if (isEmailOrLoginInDataBase(user.getEmail(), user.getUsername())) {
            throw new EmailOrLoginInDataBaseException(user.getEmail(), user.getUsername());
        }
        UserEntity userEntity = findByUsername(UserUtil.userName());
        if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
            userEntity.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null && !user.getLastName().isEmpty()) {
            userEntity.setLastName(user.getLastName());
        }
        if (user.getUsername() != null) {
            if (!user.getUsername().isEmpty()) {
                userEntity.setUsername(user.getUsername());
            } else {
                throw new CheckValueException("Login");
            }
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            if (user.getEmail().contains("@")) {
                userEntity.setEmail(user.getEmail());
            } else {
                throw new CheckValueException("Email");
            }
        }
        if (user.getPassword() != null) {
            if (!user.getPassword().isEmpty()) {
                userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                throw new CheckValueException("Password");
            }
        }
        userEntity.setUpdated(LocalDateTime.now());
        return UserMapper.INSTANCE.userToUserDtoResponse(userRepository.save(userEntity));
    }

    @Transactional
    @Override
    public List<UserDtoResponse> findAll() {
        List<UserDtoResponse> userDtoResponseList = new ArrayList<>();
        List<UserEntity> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new NoDataFoundException();
        }
        for (UserEntity user : users) {
            userDtoResponseList.add(UserMapper.INSTANCE.userToUserDtoResponse(user));
        }
        return userDtoResponseList;
    }

    @Transactional
    @Override
    public List<CompanyDto> addCompany(String symbol) {
        UserEntity user = findByUsername(UserUtil.userName());
        SubscriptionTypeDto subscriptionTypeDto = UserMapper.INSTANCE.subscriptionTypeToSubscriptionTypeDto(user.getSubscription().getType());
        return companyUserService.addCompanyToUser(symbol, user.getId(), subscriptionTypeDto);
    }

    @Transactional
    @Override
    public UserDtoResponse lockOrUnlock(Long id, String status) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setStatus(status);
        UserEntity userEntity = userRepository.save(user);
        if (status.equals(StatusUser.ACTIVE.toString())) {
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
        return UserMapper.INSTANCE.userToUserDtoResponse(userEntity);
    }

    @Transactional
    @Override
    public UserEntity findByUsername(String userName) {
        return userRepository.findByUsername(userName).orElseThrow(() ->
                new UserNotFoundException(userName));
    }

    @Transactional
    @Override
    public UserDtoResponse changeSubscription(Long subscriptionId) {
        UserEntity user = findByUsername(UserUtil.userName());
        SubscriptionEntity subscriptionEntity = user.getSubscription();
        SubscriptionTypeEntity subscriptionType = typeRepository.findById(subscriptionId).orElseThrow(SubscriptionTypeException::new);
        if (subscriptionType.getName().equals(Subscription.BASIC.toString())) {
            RoleEntity role = roleRepository.findByName(Role.ROLE_USER_INACTIVE.toString());
            List<RoleEntity> userRoles = new ArrayList<>();
            userRoles.add(role);
            user.setRoles(userRoles);
            subscriptionEntity.setType(subscriptionType);
            subscriptionEntity.setStatus(StatusSubscription.ACTIVE.toString());
            subscriptionEntity.setStartTime(LocalDateTime.now());
            subscriptionEntity.setFinishTime(LocalDateTime.now().plusYears(1));
            SubscriptionEntity saveSubscription = subscriptionRepository.save(subscriptionEntity);
            user.setSubscription(saveSubscription);
        } else {
            if (!user.getRoles().get(0).getName().equals(Role.ROLE_ADMIN.toString())) {
                RoleEntity role = roleRepository.findByName(Role.ROLE_USER.toString());
                List<RoleEntity> userRoles = new ArrayList<>();
                userRoles.add(role);
                user.setRoles(userRoles);
            }
            subscriptionEntity.setType(subscriptionType);
            subscriptionEntity.setStatus(StatusSubscription.ACTIVE.toString());
            subscriptionEntity.setStartTime(LocalDateTime.now());
            subscriptionEntity.setFinishTime(LocalDateTime.now().plusMonths(3));
            SubscriptionEntity saveSubscription = subscriptionRepository.save(subscriptionEntity);
            if (subscriptionType.getName().equals(Subscription.LOW.toString())) {
                List<CompanyDto> companyList = companyUserService.getCompaniesFromUser(user.getId());
                if (companyList.size() > 2) {
                    companyUserService.deleteOneCompanyFromList(user.getId());
                }
            }
            user.setSubscription(saveSubscription);
        }
        return UserMapper.INSTANCE.userToUserDtoResponse(userRepository.save(user));
    }

    @Transactional
    @Override
    public UserDtoResponse renewSubscription(long month) {
        UserEntity user = findByUsername(UserUtil.userName());
        SubscriptionEntity subscription = user.getSubscription();
        LocalDateTime plusMonths = subscription.getFinishTime().plusMonths(month);
        subscription.setFinishTime(plusMonths);
        SubscriptionEntity renewSubscription = subscriptionRepository.save(subscription);
        user.setSubscription(renewSubscription);
        return UserMapper.INSTANCE.userToUserDtoResponse(userRepository.save(user));
    }

    @Transactional
    @Override
    public List<CompanyDto> deleteOneCompanyFromUser(String symbol) {
        UserEntity user = findByUsername(UserUtil.userName());
        List<CompanyDto> companies = companyUserService.getCompaniesFromUser(user.getId());
        CompanyEntity companyEntity = companyRepository.findBySymbol(symbol).orElseThrow(
                () -> new CompanyNotFoundException(symbol));
        CompanyDto company = CompanyMapper.INSTANCE.companyToCompanyDto(companyEntity);
        if (companies.contains(company)) {
            companyUserService.deleteOneCompanyFromList(companyEntity.getId(), user.getId());
        } else {
            throw new CompanyIsNotOnListException(symbol);
        }
        return companyUserService.getCompaniesFromUser(user.getId());
    }

    private boolean isEmailOrLoginInDataBase(String email, String login) {
        boolean emptyLogin = userRepository.findByUsername(login).isEmpty();
        boolean emptyEmail = userRepository.findByEmail(email).isEmpty();
        return !emptyLogin || !emptyEmail;
    }
}