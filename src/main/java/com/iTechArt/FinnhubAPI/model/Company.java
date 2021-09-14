package com.iTechArt.FinnhubAPI.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "company")
public class Company extends BaseEntity {
    @Column(name = "currency")
    private String currency;

    @Column(name = "description")
    private String description;

    @Column(name = "displaySymbol")
    private String displaySymbol;

    @Column(name = "figi")
    private String figi;

    @Column(name = "mic")
    private String mic;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "type")
    private String type;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_profile_id")
    private CompanyProfile profile;

    @ManyToMany(mappedBy = "companies", fetch = FetchType.LAZY)
    private List<User> users;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Quote> quotes;


}
