package com.itechart.finnhubapi.service;

import com.itechart.finnhubapi.model.UserEntity;
import com.itechart.finnhubapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserEntity findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

}
