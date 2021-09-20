package com.itechart.finnhubapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public CustomErrorDecoder myErrorDecoder() {
        return new CustomErrorDecoder();
    }
}
