package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.RewardRuleDto;
import com.starboost.starboost_backend_demo.entity.ChallengeRewardRule;
import com.starboost.starboost_backend_demo.entity.PayoutType;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.repository.ChallengeRewardRuleRepository;
import com.starboost.starboost_backend_demo.service.ChallengeRewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeRewardServiceImpl implements ChallengeRewardService {
    private final ChallengeRewardRuleRepository repo;

    @Override
    public List<RewardRuleDto> listRewardRules(Long challengeId, Role roleCategory) {
        return repo.findAllByChallenge_IdAndRoleCategory(challengeId, roleCategory).stream()
                .map(r -> RewardRuleDto.builder()
                        .id(r.getId())
                        .roleCategory(r.getRoleCategory())
                        .payoutType(r.getPayoutType())
                        .tierMin(r.getTierMin())
                        .tierMax(r.getTierMax())
                        .baseAmount(r.getBaseAmount())
                        .gift(r.getGift())
                        .build())
                .toList();
    }

    @Override
    public List<RewardRuleDto> listAllRewardRules(Long challengeId) {
        return repo.findAllByChallenge_Id(challengeId).stream()
                .map(r -> RewardRuleDto.builder()
                        .id(r.getId())
                        .roleCategory(r.getRoleCategory())
                        .payoutType(r.getPayoutType())
                        .tierMin(r.getTierMin())
                        .tierMax(r.getTierMax())
                        .baseAmount(r.getBaseAmount())
                        .gift(r.getGift())
                        .build())
                .toList();
    }

    @Override
    public double computeReward(Long challengeId,
                                Role roleCategory,
                                Long unitCount,
                                double metricValue) {

        
        List<ChallengeRewardRule> rules =
                repo.findAllByChallenge_IdAndRoleCategory(challengeId, roleCategory);

        
        for (var r : rules) {
            if (r.getPayoutType() == PayoutType.RANK_TIERS) {
                if (metricValue >= r.getTierMin() && metricValue <= r.getTierMax()) {

                    
                    if (r.getGift() != null && !r.getGift().isBlank()) {
                        return 1.0;
                    }
                    return r.getBaseAmount();
                }
            }
        }

        
        
        if (roleCategory == Role.REGIONAL_MANAGER
                || roleCategory == Role.ANIMATOR) {

            double reward = 0.0;
            for (var r : rules) {
                if (r.getPayoutType() != PayoutType.FIXED_TIERS) {
                    continue;
                }
                double scaledMin = r.getTierMin() * unitCount;
                
                if (metricValue >= scaledMin) {
                    reward = r.getBaseAmount();
                }
            }
            return reward;
        }

        
        for (var r : rules) {
            if (r.getPayoutType() != PayoutType.PERCENT_TIERS) {
                continue;
            }
            double scaledMin = r.getTierMin() * unitCount;
            double scaledMax = r.getTierMax() * unitCount;
            if (metricValue >= scaledMin && metricValue <= scaledMax) {
                return r.getBaseAmount() * metricValue;
            }
        }

        
        for (var r : rules) {
            if (r.getPayoutType() != PayoutType.FIXED_TIERS) {
                continue;
            }
            double scaledMin = r.getTierMin() * unitCount;
            double scaledMax = r.getTierMax() * unitCount;
            if (metricValue >= scaledMin && metricValue <= scaledMax) {
                return r.getBaseAmount();
            }
        }

        
        return 0.0;
    }
}
