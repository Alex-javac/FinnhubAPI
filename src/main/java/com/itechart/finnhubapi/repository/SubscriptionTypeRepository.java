package com.itechart.finnhubapi.repository;

import com.itechart.finnhubapi.model.entity.SubscriptionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionTypeRepository extends JpaRepository<SubscriptionTypeEntity, Long> {
    Optional<SubscriptionTypeEntity> findByName(String str);
}
