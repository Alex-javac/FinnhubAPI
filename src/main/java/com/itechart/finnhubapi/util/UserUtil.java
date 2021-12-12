package com.itechart.finnhubapi.util;

import com.itechart.finnhubapi.exceptions.UserNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserUtil {
    @Value(value = "${jwt.secret}")
    private String jwtSecret;
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    public Long userID(HttpServletRequest request) {
        String token;
        String bearer = request.getHeader(AUTHORIZATION);
        if (bearer != null && bearer.startsWith(BEARER)) {
            token = bearer.substring(BEARER.length());
        } else {
            throw new UserNotFoundException("id from token");
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
        return Long.parseLong(claimsJws.getBody().getId());
    }
}