package com.itechart.finnhubapi.util;

import com.itechart.finnhubapi.model.entity.CompanyEntity;
import com.itechart.finnhubapi.model.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class UserUtil {
    public static String userName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? null : authentication.getName();
    }
    public static boolean isCompany(String symbol, UserEntity user) {
        List<CompanyEntity> companies = user.getCompanies();
        boolean flag = false;
        for (CompanyEntity company : companies) {
            if (company.getSymbol().equals(symbol)) {
                flag = true;
            }
        }
        return flag;
    }
}