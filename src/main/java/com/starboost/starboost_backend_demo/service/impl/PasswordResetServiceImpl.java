package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.ForgotPasswordRequest;
import com.starboost.starboost_backend_demo.dto.ResetPasswordRequest;
import com.starboost.starboost_backend_demo.entity.User;
import com.starboost.starboost_backend_demo.repository.UserRepository;
import com.starboost.starboost_backend_demo.service.PasswordResetService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository    userRepository;
    private final PasswordEncoder   passwordEncoder;
    private final JavaMailSender    mailSender;

    
    @Value("${jwt.reset.secret}")
    private String resetSecret;

    
    @Value("${jwt.reset.expirationMs}")
    private long resetExpirationMs;

    
    private Key resetSigningKey;

    @PostConstruct
    private void init() {
        byte[] keyBytes = resetSecret.getBytes(StandardCharsets.UTF_8);
        this.resetSigningKey = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        
        String email = request.getEmail().trim();

        
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No user registered with email: " + email)
                );

        
        String token = createResetToken(email);

        
        String link = request.getRedirectUrl() + "?token=" + token;

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Starboost Password Reset");
        msg.setText(
                "Hi " + user.getFirstName() + ",\n\n" +
                        "To reset your password, please click the link below:\n" +
                        link + "\n\n" +
                        "This link will expire in " + (resetExpirationMs / 1000 / 60) + " minutes.\n\n" +
                        "If you did not request a password reset, please ignore this email."
        );
        mailSender.send(msg);
    }

    
    private String createResetToken(String email) {
        Date now    = new Date();
        Date expiry = new Date(now.getTime() + resetExpirationMs);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(resetSigningKey)
                .compact();
    }

    @Override
    public void resetPassword(String token, ResetPasswordRequest request) {
        
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(resetSigningKey)
                .build()
                .parseClaimsJws(token);

        String email = claims.getBody().getSubject();

        
        if (request.getPassword() == null ||
                !request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords must match");
        }

        
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No user registered with email: " + email)
                );

        
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }
}
