package com.iTechArt.FinnhubAPI.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "company_profile")
public class CompanyProfile extends BaseEntity {

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
    private Company profile;
}
/**
 * {"country":"US",
 * "currency":"USD",
 * "exchange":"NASDAQ NMS - GLOBAL MARKET",
 * "finnhubIndustry":"Technology",
 * "ipo":"1980-12-12",
 * "logo":"https://finnhub.io/api/logo?symbol=AAPL",
 * "marketCapitalization":2546803,
 * "name":"Apple Inc",
 * "phone":"14089961010.0",
 * "shareOutstanding":16687.631,
 * "ticker":"AAPL",
 * "weburl":"https://www.apple.com/"}
 */
