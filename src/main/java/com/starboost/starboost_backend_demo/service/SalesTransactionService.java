package com.starboost.starboost_backend_demo.service;

import com.starboost.starboost_backend_demo.dto.SalesTransactionDto;
import com.starboost.starboost_backend_demo.entity.Role;
import java.util.List;

public interface SalesTransactionService {

    
    SalesTransactionDto create(SalesTransactionDto dto);
    SalesTransactionDto createForChallenge(Long challengeId, SalesTransactionDto dto);

    
    List<SalesTransactionDto> findAll();
    SalesTransactionDto findById(Long id);
    List<SalesTransactionDto> findAllByChallengeId(Long challengeId);

    
    SalesTransactionDto update(Long id, SalesTransactionDto dto);

    
    void deleteById(Long id);
    void deleteAll();

    
    List<SalesTransactionDto> findByChallengeAndRole(Long challengeId, Role role);
    List<SalesTransactionDto> findByChallengeAndSellerId(Long challengeId, Long sellerId);
    List<SalesTransactionDto> findByChallengeAndSellerName(Long challengeId, String sellerName);
}


