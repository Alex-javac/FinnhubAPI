package com.itechart.finnhubapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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

    @JsonBackReference
    @ManyToMany(mappedBy = "companies", fetch = FetchType.LAZY)
    private List<UserEntity> users;

    @JsonManagedReference
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<QuoteEntity> quotes;
}