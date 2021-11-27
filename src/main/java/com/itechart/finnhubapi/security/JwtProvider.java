package com.itechart.finnhubapi.security;

import com.itechart.finnhubapi.exceptions.JwtAuthenticationException;
import com.itechart.finnhubapi.model.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expired}")
    private long tokenTime;

    public String generateToken(UserEntity user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.setId(user.getId().toString());
        claims.put("login", user.getUsername());
        claims.put("password", user.getPassword());

        Date now = new Date();
        Date timeline = new Date(now.getTime() + tokenTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(timeline)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException expEx) {
            throw new JwtAuthenticationException("Token expired");
        } catch (UnsupportedJwtException unsEx) {
            throw new JwtAuthenticationException("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            throw new JwtAuthenticationException("Malformed jwt");
        } catch (SignatureException sEx) {
            throw new JwtAuthenticationException("Invalid signature");
        } catch (Exception e) {
            throw new JwtAuthenticationException("invalid token");
        }
    }

    public String getIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getId();
    }
}