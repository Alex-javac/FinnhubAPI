package com.itechart.finnhubapi.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_company")
public class CompanyUserEntity extends BaseEntity{
    @Column(name = "users_id")
    private Long usersId;
    @Column(name = "company_id")
    private Long companyId;
}