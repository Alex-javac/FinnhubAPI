package com.itechart.finnhubapi.service.impl;

import com.itechart.finnhubapi.model.*;
import com.itechart.finnhubapi.model.entity.RoleEntity;
import com.itechart.finnhubapi.model.entity.SubscriptionEntity;
import com.itechart.finnhubapi.model.entity.UserEntity;
import com.itechart.finnhubapi.repository.RoleRepository;
import com.itechart.finnhubapi.repository.UserRepository;
import com.itechart.finnhubapi.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SubscriptionServiceImpl implements SubscriptionService {
    private final UserRepository userRepository;
    private final JavaMailSender emailSender;
    private final RoleRepository roleRepository;

    public Map<String, Long> verificationSubscriptions() {
        Map<String, Long> result = new HashMap<>();
        long blocked = 0L;
        long warned = 0L;
        List<UserEntity> allUsers = userRepository.findAll();
        for (UserEntity user : allUsers) {
            if(user.getSubscription().getName().equals(Subscription.INACTIVE.toString())){
                continue;
            }
            LocalDateTime dateTime = user.getSubscription().getFinishTime();
            Duration between = Duration.between(LocalDateTime.now(), dateTime);
            if (between.isNegative() || between.isZero()) {
                RoleEntity role = roleRepository.findByName(Role.ROLE_USER_INACTIVE.toString());
                List<RoleEntity> userRoles = new ArrayList<>();
                userRoles.add(role);
                user.setRoles(userRoles);
                SubscriptionEntity subscription = user.getSubscription();
                subscription.setName(Subscription.INACTIVE.toString());
                subscription.setStartTime(null);
                subscription.setFinishTime(null);
                user.setSubscription(subscription);
                UserEntity blockedUser = userRepository.save(user);
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(blockedUser.getEmail());
                message.setSubject("FinnhubAPI");
                message.setText("subscription expired." + "\n" +
                        "to activate the subscription, follow the link: " + "http://localhost:8080/api/v1/subscription/payment");
                emailSender.send(message);
                blocked++;
            } else if (between.toDays() < 3) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(user.getEmail());
                message.setSubject("FinnhubAPI");
                message.setText("subscription expires: " + user.getSubscription().getFinishTime());
                emailSender.send(message);
                warned++;
            }
        }
        result.put("blocked", blocked);
        result.put("warned", warned);
        return result;
    }
}
