package com.itechart.finnhubapi.repository;

import com.itechart.finnhubapi.model.SubscriptionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
class SubscriptionRepositoryTest {
    @Autowired
    SubscriptionRepository subscriptionRepository;

    private final SubscriptionEntity subscription = new SubscriptionEntity();

    @BeforeEach
    void setUp() {
        subscription.setName("TEST");
        subscription.setStartTime(LocalDateTime.now());
        subscription.setFinishTime(LocalDateTime.now().plusYears(1));
    }

    @Test
    void findByName() {
        SubscriptionEntity subscriptionEntity = subscriptionRepository.save(subscription);
        Optional<SubscriptionEntity> optionalSubscription = subscriptionRepository.findByName(subscriptionEntity.getName());
        assertTrue(optionalSubscription.isPresent());
        optionalSubscription.ifPresent(entity -> {
            assertEquals(subscription.getName(), entity.getName());
        });
    }

    @Test
    void findAll() {
        final int SUBSCRIPTION_COUNT_IN_DATABASE = 5;
        List<SubscriptionEntity> resultList = subscriptionRepository.findAll();
        assertThat(resultList).isNotNull();
        assertThat(resultList.size()).isEqualTo(SUBSCRIPTION_COUNT_IN_DATABASE);
    }

    @Test
    void findById() {
        SubscriptionEntity subscriptionEntity = subscriptionRepository.save(subscription);
        Optional<SubscriptionEntity> optionalSubscription = subscriptionRepository.findById(subscriptionEntity.getId());
        assertTrue(optionalSubscription.isPresent());
        optionalSubscription.ifPresent(entity -> {
            assertEquals(subscription.getName(), entity.getName());
        });
    }

    @Test
    void save() {
        subscriptionRepository.save(subscription);
        assertNotNull(subscription.getId());
    }

    @Test
    void deleteById() {
        SubscriptionEntity subscriptionEntity = subscriptionRepository.save(subscription);
        subscriptionRepository.deleteById(subscriptionEntity.getId());
        SubscriptionEntity roleNull = subscriptionRepository.findById(subscriptionEntity.getId()).orElse(null);
        assertNull(roleNull);
    }
}