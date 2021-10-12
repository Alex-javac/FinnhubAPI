package com.itechart.finnhubapi.util;

import com.itechart.finnhubapi.model.CompanyEntity;
import com.itechart.finnhubapi.model.UserEntity;
import com.itechart.finnhubapi.repository.UserRepository;
import com.itechart.finnhubapi.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
            if(between.isNegative()|| between.isZero()){
                user.setStatus("BLOCKED");
                userRepository.save(user);
            }else if (between.toDays() < 3 ) {
                System.out.println("отправляем уведомление что скоро закончится подписка");
            }
            System.out.println("+");
        }
    }
}
