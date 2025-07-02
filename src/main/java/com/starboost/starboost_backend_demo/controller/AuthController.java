package com.starboost.starboost_backend_demo.controller;

import com.starboost.starboost_backend_demo.dto.ForgotPasswordRequest;
import com.starboost.starboost_backend_demo.dto.LoginRequest;
import com.starboost.starboost_backend_demo.dto.LoginResponse;
import com.starboost.starboost_backend_demo.dto.ResetPasswordRequest;
import com.starboost.starboost_backend_demo.service.PasswordResetService;
import com.starboost.starboost_backend_demo.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    
    private final AuthenticationManager authenticationManager;

    
    private final JwtUtil jwtUtil;

    
    private final PasswordResetService passwordResetService;

    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest req) {

        
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(),    
                        req.getPassword()  
                );

        
        Authentication auth = authenticationManager.authenticate(authToken);

        
        SecurityContextHolder.getContext().setAuthentication(auth);

        
        String jwt = jwtUtil.generateToken(auth);

        
        return ResponseEntity.ok(new LoginResponse(jwt));
    }

    
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest req) {

        passwordResetService.forgotPassword(req);
        return ResponseEntity.ok().build();
    }

    
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @Valid @RequestBody ResetPasswordRequest req) {

        passwordResetService.resetPassword(req.getToken(), req);
        return ResponseEntity.ok().build();
    }
}
