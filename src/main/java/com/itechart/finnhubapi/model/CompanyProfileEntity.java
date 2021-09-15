package com.itechart.finnhubapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "company_profile")
public class CompanyProfileEntity extends BaseEntity {

    @Column(name = "country")
    private String country;

    @Column(name = "currency")
    private String currency;

    @Column(name = "exchange")
    private String exchange;

    @Column(name = "finnhub_industry")
    private String finnhubIndustry;

    @Column(name = "ipo")
    private Date ipo;

    @Column(name = "logo")
    private String logo;

    @Column(name = "market_capitalization")
    private String marketCapitalization;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "share_outstanding")
    private String shareOutstanding;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "weburl")
    private String weburl;

    @OneToOne(mappedBy = "profile")
    private CompanyEntity profile;
}