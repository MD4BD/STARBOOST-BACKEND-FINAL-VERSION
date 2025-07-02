package com.starboost.starboost_backend_demo.controller;

import com.starboost.starboost_backend_demo.dto.ChallengeLeaderboardDto;
import com.starboost.starboost_backend_demo.service.ChallengeLeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/challenges/{challengeId}/leaderboard")
@RequiredArgsConstructor
public class ChallengeLeaderboardController {

    private final ChallengeLeaderboardService leaderboardService;

    
    @GetMapping("/agents")
    public List<ChallengeLeaderboardDto> agents(
            @PathVariable Long challengeId,
            @RequestParam(value = "filterId",   required = false) Long userId,
            @RequestParam(value = "filterName", required = false) String name
    ) {
        
        List<ChallengeLeaderboardDto> list = leaderboardService.agents(challengeId);

        
        if (userId != null) {
            list = list.stream()
                    .filter(e -> userId.equals(e.getUserId()))
                    .collect(Collectors.toList());
        }

        
        if (name != null && !name.isBlank()) {
            String lower = name.toLowerCase();
            list = list.stream()
                    .filter(e -> e.getName().toLowerCase().contains(lower))
                    .collect(Collectors.toList());
        }

        return list;
    }

    
    @GetMapping("/commercials")
    public List<ChallengeLeaderboardDto> commercials(
            @PathVariable Long challengeId,
            @RequestParam(value = "filterId",   required = false) Long userId,
            @RequestParam(value = "filterName", required = false) String name
    ) {
        List<ChallengeLeaderboardDto> list = leaderboardService.commercials(challengeId);

        if (userId != null) {
            list = list.stream()
                    .filter(e -> userId.equals(e.getUserId()))
                    .collect(Collectors.toList());
        }
        if (name != null && !name.isBlank()) {
            String lower = name.toLowerCase();
            list = list.stream()
                    .filter(e -> e.getName().toLowerCase().contains(lower))
                    .collect(Collectors.toList());
        }

        return list;
    }

    
    @GetMapping("/agencies")
    public List<ChallengeLeaderboardDto> agencies(
            @PathVariable Long challengeId,
            @RequestParam(value = "filterId",   required = false) Long agencyId,
            @RequestParam(value = "filterName", required = false) String name
    ) {
        List<ChallengeLeaderboardDto> list = leaderboardService.agencies(challengeId);

        if (agencyId != null) {
            list = list.stream()
                    .filter(e -> agencyId.equals(e.getAgencyId()))
                    .collect(Collectors.toList());
        }
        if (name != null && !name.isBlank()) {
            String lower = name.toLowerCase();
            list = list.stream()
                    .filter(e -> e.getName().toLowerCase().contains(lower))
                    .collect(Collectors.toList());
        }

        return list;
    }

    
    @GetMapping("/regions")
    public List<ChallengeLeaderboardDto> regions(
            @PathVariable Long challengeId,
            @RequestParam(value = "filterId",   required = false) Long regionId,
            @RequestParam(value = "filterName", required = false) String name
    ) {
        List<ChallengeLeaderboardDto> list = leaderboardService.regions(challengeId);

        if (regionId != null) {
            list = list.stream()
                    .filter(e -> regionId.equals(e.getRegionId()))
                    .collect(Collectors.toList());
        }
        if (name != null && !name.isBlank()) {
            String lower = name.toLowerCase();
            list = list.stream()
                    .filter(e -> e.getName().toLowerCase().contains(lower))
                    .collect(Collectors.toList());
        }

        return list;
    }
}
