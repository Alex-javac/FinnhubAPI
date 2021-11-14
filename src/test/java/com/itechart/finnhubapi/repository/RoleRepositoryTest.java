package com.itechart.finnhubapi.repository;

import com.itechart.finnhubapi.model.RoleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class RoleRepositoryTest {
    @Autowired
    RoleRepository roleRepository;

    private final RoleEntity role = new RoleEntity();

    @BeforeEach
    void setUp() {
        role.setName("ROLE_TEST");
    }

    @Test
    void findByName() {
        RoleEntity roleEntity = roleRepository.save(role);
        RoleEntity roleByName = roleRepository.findByName(roleEntity.getName());
        assertThat(roleEntity).isEqualTo(roleByName);
    }

    @Test
    void findAll() {
        final int ROLES_COUNT_IN_DATABASE = 3;
        List<RoleEntity> resultList = roleRepository.findAll();
        assertThat(resultList).isNotNull();
        assertThat(resultList.size()).isEqualTo(ROLES_COUNT_IN_DATABASE);
    }

    @Test
    void findById() {
        RoleEntity roleEntity = roleRepository.save(role);
        Optional<RoleEntity> optionalCompany = roleRepository.findById(roleEntity.getId());
        assertTrue(optionalCompany.isPresent());
        optionalCompany.ifPresent(entity -> {
            assertEquals(role.getName(), entity.getName());
        });
    }

    @Test
    void save() {
        roleRepository.save(role);
        assertNotNull(role.getId());
    }

    @Test
    void deleteById() {
        RoleEntity roleEntity = roleRepository.save(role);
        roleRepository.deleteById(roleEntity.getId());
        RoleEntity roleNull = roleRepository.findById(roleEntity.getId()).orElse(null);
        assertNull(roleNull);
    }
}