package com.starboost.starboost_backend_demo.service;

import com.starboost.starboost_backend_demo.dto.WinnerDto;
import com.starboost.starboost_backend_demo.entity.Role;

import java.util.List;


public interface ChallengeEvaluationService {

    
    List<WinnerDto> listWinners(Long challengeId);

    
    List<WinnerDto> listWinnersByRole(Long challengeId, Role roleCategory);
}
