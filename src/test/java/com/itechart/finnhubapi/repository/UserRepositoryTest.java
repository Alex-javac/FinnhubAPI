package com.itechart.finnhubapi.repository;

import com.itechart.finnhubapi.model.RoleEntity;
import com.itechart.finnhubapi.model.Subscription;
import com.itechart.finnhubapi.model.SubscriptionEntity;
import com.itechart.finnhubapi.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    SubscriptionRepository subscriptionRepository;
    @Autowired
    RoleRepository roleRepository;

    private final UserEntity user = new UserEntity();
    private final SubscriptionEntity subscription = new SubscriptionEntity();
    private final RoleEntity role = new RoleEntity();

    @BeforeEach
    void setUp() {
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
        subscriptionRepository.save(subscription);
        user.setSubscription(subscription);
        role.setName("ROLE_TEST");
        roleRepository.save(role);
        List<RoleEntity> listRole = new ArrayList<>();
        listRole.add(role);
        user.setRoles(listRole);
    }

    @Test
    void findByUsername() {
        UserEntity userEntity = userRepository.save(user);
        Optional<UserEntity> optionalUser = userRepository.findByUsername(userEntity.getUsername());
        assertTrue(optionalUser.isPresent());
        optionalUser.ifPresent(entity -> {
            assertEquals(user.getUsername(), entity.getUsername());
        });
    }

    @Test
    void findById() {
        UserEntity userEntity = userRepository.save(user);
        Optional<UserEntity> optionalUser = userRepository.findById(userEntity.getId());
        assertTrue(optionalUser.isPresent());
        optionalUser.ifPresent(entity -> {
            assertEquals(user.getUsername(), entity.getUsername());
        });
    }

    @Test
    void save() {
        UserEntity userEntity = userRepository.save(user);
        assertNotNull(userEntity.getId());
    }

    @Test
    void findAll() {
        final int USERS_COUNT_IN_DATABASE = 3;
        List<UserEntity> resultList = userRepository.findAll();
        assertThat(resultList).isNotNull();
        assertThat(resultList.size()).isEqualTo(USERS_COUNT_IN_DATABASE);
    }

    @Test
    void deleteById() {
        UserEntity userEntity = userRepository.save(user);
        userRepository.deleteById(userEntity.getId());
        UserEntity userNull = userRepository.findById(userEntity.getId()).orElse(null);
        assertNull(userNull);
    }
}