package com.itechart.finnhubapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "company")
public class CompanyEntity extends BaseEntity {
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

    @ManyToMany(mappedBy = "companies", fetch = FetchType.LAZY)
    private List<UserEntity> users;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<QuoteEntity> quotes;
}