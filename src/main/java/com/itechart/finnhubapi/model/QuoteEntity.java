package com.itechart.finnhubapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "quote")
public class QuoteEntity extends BaseEntity {
    @Column(name = "c")
    private double c;//Current price

    @Column(name = "d")
    private double d;//Change

    @Column(name = "dp")
    private double dp;//Percent change

    @Column(name = "h")
    private double h;//High price of the day

    @Column(name = "l")
    private double l;//Low price of the day

    @Column(name = "o")
    private double o;//Open price of the day

    @Column(name = "pc")
    private double pc;//Previous close price

    @Column(name = "t")
    private double t;//????

    @Column(name = "date")
    private Date date;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity company;
}