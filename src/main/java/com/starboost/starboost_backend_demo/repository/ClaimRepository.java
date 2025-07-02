package com.starboost.starboost_backend_demo.repository;

import com.starboost.starboost_backend_demo.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByUserIdAndCreatedAtBetween(Long userId,
                                                LocalDateTime from,
                                                LocalDateTime to);

    List<Claim> findByCreatedAtBetween(LocalDateTime from,
                                       LocalDateTime to);

    List<Claim> findByReadFalse();

    List<Claim> findByReadFalseAndCreatedAtBetween(LocalDateTime from,
                                                   LocalDateTime to);
}
