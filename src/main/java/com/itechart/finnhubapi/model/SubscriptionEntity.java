package com.itechart.finnhubapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "subscription")
public class SubscriptionEntity extends BaseEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "finish_time")
    private Date finishTime;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<UserEntity> users;
}
