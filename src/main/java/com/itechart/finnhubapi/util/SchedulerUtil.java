package com.itechart.finnhubapi.util;

import com.itechart.finnhubapi.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SchedulerUtil {
    private final SubscriptionService subscriptionService;

    @Scheduled(cron = "0 0 2 * * ?") //0 * * ? * * - каждую минуту(для проверки)
    public void schedulerVerificationSubscriptions() {
       subscriptionService.verificationSubscriptions();
    }
}