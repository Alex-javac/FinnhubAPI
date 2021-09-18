package com.itechart.finnhubapi.service;

import com.itechart.finnhubapi.model.RoleEntity;
import com.itechart.finnhubapi.model.UserEntity;
import com.itechart.finnhubapi.repository.RoleRepository;
import com.itechart.finnhubapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
//    private final BCryptPasswordEncoder passwordEncoder;

    public UserEntity findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    public UserEntity saveUser(UserEntity user) {
        RoleEntity role = roleRepository.findByName("ROLE_USER");
        List<RoleEntity> userRoles = new ArrayList<>();
        userRoles.add(role);
        user.setRoles(userRoles);
        user.setPassword(user.getPassword());
        UserEntity registeredUser = userRepository.save(user);
        return registeredUser;
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

}
