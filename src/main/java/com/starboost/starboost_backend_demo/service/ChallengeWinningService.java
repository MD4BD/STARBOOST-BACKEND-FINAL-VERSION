package com.starboost.starboost_backend_demo.service;

import com.starboost.starboost_backend_demo.dto.WinningRuleDto;
import java.util.List;


public interface ChallengeWinningService {
        
    List<WinningRuleDto> listWinningRules(Long challengeId);
}
