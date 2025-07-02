package com.starboost.starboost_backend_demo.repository;

import com.starboost.starboost_backend_demo.entity.SalesTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesTransactionRepository extends JpaRepository<SalesTransaction, Long> {


    List<SalesTransaction> findAllByChallenge_Id(Long challengeId);

    long countByChallenge_IdAndSellerId(Long challengeId, Long sellerId);

    @Query("""
        SELECT COALESCE(SUM(t.premium), 0)
        FROM SalesTransaction t
        WHERE t.challenge.id = :challengeId
          AND t.sellerId      = :sellerId
    """)
    double sumPremiumByChallengeIdAndSellerId(
            @Param("challengeId") Long challengeId,
            @Param("sellerId")    Long sellerId
    );
}
