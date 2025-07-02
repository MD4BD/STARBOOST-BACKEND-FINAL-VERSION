package com.starboost.starboost_backend_demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    
    @Value("${jwt.expirationMs}")
    private long jwtExpirationMs;
    
    @Value("${jwt.reset.secret}")
    private String jwtResetSecret;

    
    @Value("${jwt.reset.expirationMs}")
    private long jwtResetExpirationMs;

    
    private Key signingKey;
    
    private Key resetSigningKey;

    @PostConstruct
    public void init() {
        byte[] authKeyBytes  = jwtSecret.getBytes(StandardCharsets.UTF_8);
        this.signingKey      = Keys.hmacShaKeyFor(authKeyBytes);

        byte[] resetKeyBytes = jwtResetSecret.getBytes(StandardCharsets.UTF_8);
        this.resetSigningKey = Keys.hmacShaKeyFor(resetKeyBytes);
    }

    
    public String generateToken(org.springframework.security.core.Authentication auth) {
        var userDetails = (org.springframework.security.core.userdetails.User) auth.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(granted -> granted.getAuthority())
                .collect(Collectors.toList());

        Date now    = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())   
                .claim("roles", roles)                   
                .setIssuedAt(now)                        
                .setExpiration(expiry)                   
                .signWith(signingKey)                    
                .compact();
    }

    
    public String getUsernameFromToken(String token) {
        return parse(token, signingKey).getBody().getSubject();
    }

    
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        return (List<String>) parse(token, signingKey).getBody().get("roles");
    }

    
    public boolean validateToken(String token) {
        try {
            parse(token, signingKey);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    

    public String generateResetToken(String email) {
        Date now    = new Date();
        Date expiry = new Date(now.getTime() + jwtResetExpirationMs);

        return Jwts.builder()
                .setSubject(email)                      
                .setIssuedAt(now)                       
                .setExpiration(expiry)                  
                .signWith(resetSigningKey)              
                .compact();
    }

    
    public String getEmailFromResetToken(String token) {
        return parse(token, resetSigningKey).getBody().getSubject();
    }

    
    public boolean validateResetToken(String token) {
        try {
            parse(token, resetSigningKey);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }


    private Jws<Claims> parse(String token, Key key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}
