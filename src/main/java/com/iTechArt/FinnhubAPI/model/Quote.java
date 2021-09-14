package com.iTechArt.FinnhubAPI.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "quote")
public class Quote extends BaseEntity {
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

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
/**
 * {"c":148.97,
 * "d":-5.1,
 * "dp":-3.3102,
 * "h":155.48,
 * "l":148.7,
 * "o":155,
 * "pc":154.07,
 * "t":1631304001}
 */