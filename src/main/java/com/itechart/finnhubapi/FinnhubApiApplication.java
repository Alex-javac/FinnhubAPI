package com.itechart.finnhubapi;

import com.itechart.finnhubapi.dto.*;
import com.itechart.finnhubapi.dto.financialdto.FinancialStatementDto;
import com.itechart.finnhubapi.dto.metricdto.MetricDto;
import com.itechart.finnhubapi.feignservice.ServiceFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.List;


@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class FinnhubApiApplication {

	public FinnhubApiApplication(ServiceFeignClient serviceFeignClient) {
		this.serviceFeignClient = serviceFeignClient;
	}

	private static ServiceFeignClient serviceFeignClient;

	public static void main(String[] args) {
		SpringApplication.run(FinnhubApiApplication.class, args);
//String token ="btqbebn48v6t9hdd6cog";
//String symbol ="AAPL";
//		List<CompanyDto> companyEntities = serviceFeignClient.getCompany(token);
//		companyEntities.forEach(System.out::println);
//		System.out.println("______________________________________________________________________________");
//		MetricDto metricDtoList = serviceFeignClient.getMetric(symbol,token);
//		System.out.println(metricDtoList);
//		System.out.println("______________________________________________________________________________");
//		FinancialStatementDto finance = serviceFeignClient.getFinance(symbol,token);
//		System.out.println(finance);
//		System.out.println("______________________________________________________________________________");
//		QuoteDto quote = serviceFeignClient.getQuote(symbol,token);
//		System.out.println(quote);
	}



}
