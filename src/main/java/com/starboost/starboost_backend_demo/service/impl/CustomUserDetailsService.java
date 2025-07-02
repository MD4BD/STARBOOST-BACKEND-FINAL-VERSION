package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.entity.User;
import com.starboost.starboost_backend_demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        
        User user = userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No user with email: " + email)
                );

        
        if (!user.isActive()) {
            
            throw new DisabledException("Account is inactive");
        }

        
        GrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(authority)
                .accountLocked(false)
                .accountExpired(false)
                .credentialsExpired(false)                
                .disabled(false)
                .build();
    }
}
