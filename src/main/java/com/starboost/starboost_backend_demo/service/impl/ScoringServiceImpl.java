package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.entity.Challenge;
import com.starboost.starboost_backend_demo.entity.SalesTransaction;
import com.starboost.starboost_backend_demo.entity.ScoreRule;
import com.starboost.starboost_backend_demo.repository.ChallengeRepository;
import com.starboost.starboost_backend_demo.repository.SalesTransactionRepository;
import com.starboost.starboost_backend_demo.repository.ScoreRuleRepository;
import com.starboost.starboost_backend_demo.service.ScoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class ScoringServiceImpl implements ScoringService {

    private final ChallengeRepository        challengeRepo;
    private final SalesTransactionRepository txRepo;
    private final ScoreRuleRepository        scoreRuleRepo;

    @Override
    public Map<Long, Integer> calculateScores(Long challengeId) {
        
        Challenge challenge = challengeRepo.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("Challenge not found: " + challengeId));
        LocalDateTime start = challenge.getStartDate().atStartOfDay();
        LocalDateTime end   = challenge.getEndDate().plusDays(1).atStartOfDay();

        
        List<ScoreRule> scoreRules = scoreRuleRepo.findAllByChallenge_Id(challengeId);
        System.out.println(">>> SCORES FOR CHALLENGE "
                + challengeId + " → " + scoreRules);

        
        
        List<SalesTransaction> allTxs = txRepo.findAllByChallenge_Id(challengeId);
        System.out.println("ALL TXS for challenge " + challengeId + " → " + allTxs);

        
        List<SalesTransaction> txs = allTxs.stream()
                .filter(t -> !t.getSaleDate().isBefore(start) && t.getSaleDate().isBefore(end))
                .toList();
        System.out.println("FILTERED TXS for challenge " + challengeId + " → " + txs);

        Map<Long,Integer> scores = new HashMap<>();

        for (SalesTransaction tx : txs) {
            
            boolean matches = challenge.getRules().stream().anyMatch(r ->
                    (r.getContractType()      == null || r.getContractType()      == tx.getContractType()) &&
                            (r.getTransactionNature() == null || r.getTransactionNature() == tx.getTransactionNature()) &&
                            (r.getPackType()          == null )
            );
            if (!matches) continue;

            
            int txScore = 0;
            for (ScoreRule sr : scoreRules) {
                switch (sr.getScoreType()) {
                    case CONTRACT:
                        if (sr.getContractType() == tx.getContractType()) {
                            txScore += sr.getPoints();
                        }
                        break;
                    case PACK:
                        
                        break;
                    case REVENUE:
                        txScore += (int)((tx.getPremium() / sr.getRevenueUnit()) * sr.getPoints());
                        break;
                }
            }

            
            scores.merge(tx.getSellerId(), txScore, Integer::sum);
        }

        System.out.println("FINAL SCORES for challenge " + challengeId + " → " + scores);
        return scores;
    }
}
