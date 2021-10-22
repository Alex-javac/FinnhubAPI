package com.itechart.finnhubapi.util;

import com.itechart.finnhubapi.model.CompanyEntity;
import com.itechart.finnhubapi.model.UserEntity;
import com.itechart.finnhubapi.repository.UserRepository;
import com.itechart.finnhubapi.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SchedulerUtil {
    private final CompanyService companyService;
    private final UserRepository userRepository;
    private final JavaMailSender emailSender;

    @Scheduled(cron = "0 0 1 * * ?")
    public void renewalTrades() {
        List<CompanyEntity> company = companyService.findAll();
        companyService.saveQuote(company);
    }

    @Scheduled(cron = "0 0 2 * * ?") //0 * * ? * * - каждую минуту(для проверки)
    public void verificationSubscriptions() {
        List<UserEntity> allUsers = userRepository.findAll();
        for (UserEntity user : allUsers) {
            LocalDateTime dateTime = user.getSubscription().getFinishTime();
            Duration between = Duration.between(LocalDateTime.now(), dateTime);
            if (between.isNegative() || between.isZero()) {
                user.setStatus("BLOCKED");
                UserEntity blockedUser = userRepository.save(user);
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(blockedUser.getEmail());
                message.setSubject("FinnhubAPI");
                message.setText("subscription expired. You are blocked");
                emailSender.send(message);
            } else if (between.toDays() < 3) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(user.getEmail());
                message.setSubject("FinnhubAPI");
                message.setText("subscription expires: " + user.getSubscription().getFinishTime());
                emailSender.send(message);
            }
            System.out.println("+");
        }
    }
}
