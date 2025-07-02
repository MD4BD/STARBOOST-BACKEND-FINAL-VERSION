package com.starboost.starboost_backend_demo.controller;

import com.starboost.starboost_backend_demo.dto.SalesTransactionDto;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.service.SalesTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/challenges/{challengeId}/sales")
@RequiredArgsConstructor
public class SalesTransactionController {
    private final SalesTransactionService service;

    @PostMapping
    public ResponseEntity<SalesTransactionDto> create(
            @PathVariable Long challengeId,
            @Valid @RequestBody SalesTransactionDto dto) {
        
        dto.setChallengeId(challengeId);

        SalesTransactionDto created = service.createForChallenge(challengeId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<SalesTransactionDto>> getAll(
            @PathVariable Long challengeId) {
        return ResponseEntity.ok(service.findAllByChallengeId(challengeId));
    }

    
    @GetMapping("/agents")
    public ResponseEntity<List<SalesTransactionDto>> getAgents(
            @PathVariable Long challengeId,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name) {
        List<SalesTransactionDto> list;
        if (id != null) {
            list = service.findByChallengeAndSellerId(challengeId, id);
        } else if (name != null && !name.isBlank()) {
            list = service.findByChallengeAndSellerName(challengeId, name);
        } else {
            list = service.findByChallengeAndRole(challengeId, Role.AGENT);
        }
        return ResponseEntity.ok(list);
    }

    
    @GetMapping("/commercials")
    public ResponseEntity<List<SalesTransactionDto>> getCommercials(
            @PathVariable Long challengeId,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name) {
        List<SalesTransactionDto> list;
        if (id != null) {
            list = service.findByChallengeAndSellerId(challengeId, id);
        } else if (name != null && !name.isBlank()) {
            list = service.findByChallengeAndSellerName(challengeId, name);
        } else {
            list = service.findByChallengeAndRole(challengeId, Role.COMMERCIAL);
        }
        return ResponseEntity.ok(list);
    }
}