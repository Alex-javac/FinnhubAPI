package com.itechart.finnhubapi.repository;

import com.itechart.finnhubapi.model.entity.CompanyUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyUserRepository extends JpaRepository<CompanyUserEntity, Long> {
    Optional<List<CompanyUserEntity>> findByUsersId(Long id);
   Optional<CompanyUserEntity> findByUsersIdAndCompanyId(Long UserId,Long companyId);
}
