package com.itechart.finnhubapi.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.itechart.finnhubapi.model.StatusSubscription;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "subscription")
public class SubscriptionEntity extends BaseEntity {
    @Column(name = "status")
    private String status;
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "finish_time")
    private LocalDateTime finishTime;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subscription_type_id")
    private SubscriptionTypeEntity type;

    @JsonBackReference
    @OneToOne(mappedBy = "subscription")
    private UserEntity user;
}