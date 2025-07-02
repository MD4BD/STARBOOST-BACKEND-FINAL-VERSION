package com.starboost.starboost_backend_demo.controller;

import com.starboost.starboost_backend_demo.dto.ChallengeParticipantDto;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.service.ChallengeParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/challenges/{challengeId}/participants")
@RequiredArgsConstructor
public class ChallengeParticipantController {
    private final ChallengeParticipantService participantService;

    
    @GetMapping
    public ResponseEntity<List<ChallengeParticipantDto>> all(
            @PathVariable Long challengeId,
            @RequestParam(required = false) Long filterId,
            @RequestParam(required = false) String filterName
    ) {
        
        List<ChallengeParticipantDto> list = participantService.findByChallengeId(challengeId);

        
        if (filterId != null) {
            list = list.stream()
                    .filter(p -> p.getUserId().equals(filterId)
                            || p.getParticipantId().equals(filterId))
                    .toList();
        }

        
        if (filterName != null && !filterName.isBlank()) {
            String lower = filterName.toLowerCase();
            list = list.stream()
                    .filter(p -> (p.getFirstName() + " " + p.getLastName())
                            .toLowerCase()
                            .contains(lower))
                    .toList();
        }

        return ResponseEntity.ok(list);
    }

    
    @GetMapping("/agents")
    public ResponseEntity<List<ChallengeParticipantDto>> agents(
            @PathVariable Long challengeId,
            @RequestParam(required = false) Long filterId,
            @RequestParam(required = false) String filterName
    ) {
        return ResponseEntity.ok(
                participantService.findByChallengeAndRole(
                        challengeId, Role.AGENT, filterId, filterName
                )
        );
    }

    
    @GetMapping("/commercials")
    public ResponseEntity<List<ChallengeParticipantDto>> commercials(
            @PathVariable Long challengeId,
            @RequestParam(required = false) Long filterId,
            @RequestParam(required = false) String filterName
    ) {
        return ResponseEntity.ok(
                participantService.findByChallengeAndRole(
                        challengeId, Role.COMMERCIAL, filterId, filterName
                )
        );
    }

    
    @GetMapping("/agency-managers")
    public ResponseEntity<List<ChallengeParticipantDto>> agencyManagers(
            @PathVariable Long challengeId,
            @RequestParam(required = false) Long filterId,
            @RequestParam(required = false) String filterName
    ) {
        return ResponseEntity.ok(
                participantService.findByChallengeAndRole(
                        challengeId, Role.AGENCY_MANAGER, filterId, filterName
                )
        );
    }

    
    @GetMapping("/regional-managers")
    public ResponseEntity<List<ChallengeParticipantDto>> regionalManagers(
            @PathVariable Long challengeId,
            @RequestParam(required = false) Long filterId,
            @RequestParam(required = false) String filterName
    ) {
        return ResponseEntity.ok(
                participantService.findByChallengeAndRole(
                        challengeId, Role.REGIONAL_MANAGER, filterId, filterName
                )
        );
    }

    
    @GetMapping("/animators")
    public ResponseEntity<List<ChallengeParticipantDto>> animators(
            @PathVariable Long challengeId,
            @RequestParam(required = false) Long filterId,
            @RequestParam(required = false) String filterName
    ) {
        return ResponseEntity.ok(
                participantService.findByChallengeAndRole(
                        challengeId, Role.ANIMATOR, filterId, filterName
                )
        );
    }
}
