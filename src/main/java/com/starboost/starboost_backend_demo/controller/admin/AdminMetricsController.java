package com.starboost.starboost_backend_demo.controller.admin;

import com.starboost.starboost_backend_demo.dto.WinnerDto;
import com.starboost.starboost_backend_demo.dto.ChallengeParticipantDto;
import com.starboost.starboost_backend_demo.dto.PerformanceDto;
import com.starboost.starboost_backend_demo.service.ChallengeEvaluationService;
import com.starboost.starboost_backend_demo.service.ChallengeParticipantService;
import com.starboost.starboost_backend_demo.service.ChallengePerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminMetricsController {

    private final ChallengeEvaluationService evalService;
    private final ChallengeParticipantService partService;
    private final ChallengePerformanceService perfService;

    @GetMapping("/leaderboards")
    public List<WinnerDto> leaderboards(@RequestParam Long challengeId) {
        return evalService.listWinners(challengeId);
    }

    @GetMapping("/participants")
    public List<ChallengeParticipantDto> participants(@RequestParam Long challengeId) {
        return partService.findByChallengeId(challengeId);
    }

    @GetMapping("/performance")
    public List<PerformanceDto> performance(@RequestParam Long challengeId) {
        return perfService.agents(challengeId, null, null);
    }
}
