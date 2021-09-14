package com.iTechArt.FinnhubAPI.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "subscription")
public class Subscription extends BaseEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "finish_time")
    private Date finishTime;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<User> users;
}
