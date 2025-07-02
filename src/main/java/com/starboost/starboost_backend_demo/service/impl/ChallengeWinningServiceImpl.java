package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.WinningRuleDto;
import com.starboost.starboost_backend_demo.entity.ChallengeWinningRule;
import com.starboost.starboost_backend_demo.repository.ChallengeWinningRuleRepository;
import com.starboost.starboost_backend_demo.service.ChallengeWinningService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ChallengeWinningServiceImpl implements ChallengeWinningService {
    private final ChallengeWinningRuleRepository repo;

    @Override
    public List<WinningRuleDto> listWinningRules(Long challengeId) {
        return repo.findAllByChallenge_Id(challengeId).stream()
                .map(rule -> WinningRuleDto.builder()
                        .id(rule.getId())
                        .roleCategory(rule.getRoleCategory())
                        .conditionType(rule.getConditionType())
                        .thresholdMin(rule.getThresholdMin())
                        .build())
                .toList();
    }
}
