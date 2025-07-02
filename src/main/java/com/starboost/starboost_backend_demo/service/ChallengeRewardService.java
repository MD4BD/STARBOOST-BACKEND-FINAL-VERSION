package com.starboost.starboost_backend_demo.service;

import com.starboost.starboost_backend_demo.dto.RewardRuleDto;
import com.starboost.starboost_backend_demo.entity.Role;
import java.util.List;


public interface ChallengeRewardService {

    
    List<RewardRuleDto> listRewardRules(Long challengeId, Role roleCategory);

    
    double computeReward(Long challengeId,
                         Role roleCategory,
                         Long unitCount,
                         double metricValue);

    List<RewardRuleDto> listAllRewardRules(Long challengeId);

}
