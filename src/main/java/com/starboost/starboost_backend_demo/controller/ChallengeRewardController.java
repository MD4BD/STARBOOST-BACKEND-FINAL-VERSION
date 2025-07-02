package com.starboost.starboost_backend_demo.controller;

import com.starboost.starboost_backend_demo.dto.RewardRuleDto;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.service.ChallengeRewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/challenges/{challengeId}/rewards")
@RequiredArgsConstructor
public class ChallengeRewardController {
    private final ChallengeRewardService service;

    
     @GetMapping
     public List<RewardRuleDto> listAll(
     @PathVariable Long challengeId
     ) {
     return service.listAllRewardRules(challengeId);
     }
     
    @GetMapping("/{role}")
    public List<RewardRuleDto> listRewardRules(
            @PathVariable Long challengeId,
            @PathVariable("role") Role roleCategory
    ) {
        return service.listRewardRules(challengeId, roleCategory);
    }

    
    @GetMapping("/{role}/compute")
    public double computeReward(
            @PathVariable Long challengeId,
            @PathVariable("role") Role roleCategory,
            @RequestParam Long unitCount,
            @RequestParam double metricValue
    ) {
        return service.computeReward(challengeId, roleCategory, unitCount, metricValue);
    }
}
