package com.itechart.finnhubapi.repository;

import com.itechart.finnhubapi.model.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

    Optional<SubscriptionEntity> findByType(String str);
}