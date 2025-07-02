package com.starboost.starboost_backend_demo.controller;

import com.starboost.starboost_backend_demo.dto.WinnerDto;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.service.ChallengeEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/challenges/{challengeId}")
@RequiredArgsConstructor
public class ChallengeEvaluationController {

    private final ChallengeEvaluationService evaluationService;

    
    @GetMapping("/winners")
    public List<WinnerDto> listWinners(@PathVariable Long challengeId) {
        return evaluationService.listWinners(challengeId);
    }

    
    @GetMapping("/winners/{role}")
    public List<WinnerDto> listWinnersByRole(
            @PathVariable Long challengeId,
            @PathVariable Role role
    ) {
        
        List<WinnerDto> initialList = evaluationService.listWinnersByRole(challengeId, role);

        
        
        return initialList.stream()
                .filter(w -> w.getRewardAmount() > 0)
                .collect(Collectors.toList());
    }
}
