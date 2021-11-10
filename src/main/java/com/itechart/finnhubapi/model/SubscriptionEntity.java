package com.itechart.finnhubapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
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
    private LocalDateTime startTime;
    @Column(name = "finish_time")
    private LocalDateTime finishTime;

    @JsonBackReference
    @OneToMany(mappedBy = "subscription", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserEntity> users;
}