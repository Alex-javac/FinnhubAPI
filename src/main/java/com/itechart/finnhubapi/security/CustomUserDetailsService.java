package com.itechart.finnhubapi.security;

import com.itechart.finnhubapi.exceptions.UserNotFoundException;
import com.itechart.finnhubapi.model.entity.UserEntity;
import com.itechart.finnhubapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public CustomUserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        long userID = Long.parseLong(id);
        UserEntity user = userRepo.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));
        return CustomUserDetails.fromUser(user);
    }
}