package com.starboost.starboost_backend_demo.controller;

import com.starboost.starboost_backend_demo.dto.WinningRuleDto;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.service.ChallengeWinningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/api/challenges/{challengeId}/winning-rules",
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ChallengeWinningController {

    private final ChallengeWinningService winningService;

    
    @GetMapping
    public List<WinningRuleDto> listAll(
            @PathVariable Long challengeId
    ) {
        return winningService.listWinningRules(challengeId);
    }

    
    @GetMapping("/{roleCategory}")
    public List<WinningRuleDto> listByRole(
            @PathVariable Long challengeId,
            @PathVariable String roleCategory
    ) {
        
        Role role = Role.valueOf(roleCategory.toUpperCase());

        
        return winningService.listWinningRules(challengeId).stream()
                .filter(dto -> dto.getRoleCategory() == role)
                .collect(Collectors.toList());
    }
}
