package com.itechart.finnhubapi.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.itechart.finnhubapi.model.Subscription;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "subscription_type")
public class SubscriptionTypeEntity extends BaseEntity{
    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @JsonBackReference
    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<SubscriptionEntity> subscriptionEntities;
}
