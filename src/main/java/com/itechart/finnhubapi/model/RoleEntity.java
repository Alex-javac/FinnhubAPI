package com.itechart.finnhubapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class RoleEntity extends BaseEntity {
    @Column(name = "name")
    private String name;

    @JsonBackReference
    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    private List<UserEntity> users;
}
