package com.itechart.finnhubapi.repository;

import com.itechart.finnhubapi.model.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
}
