package com.itechart.finnhubapi.repository;

import com.itechart.finnhubapi.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
