// src/main/java/com/starboost/starboost_backend_demo/repository/UserRepository.java
package com.starboost.starboost_backend_demo.repository;

import com.starboost.starboost_backend_demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByEmail(String email);
}
