
package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.*;
import com.starboost.starboost_backend_demo.entity.*;
import com.starboost.starboost_backend_demo.repository.ChallengeRepository;
import com.starboost.starboost_backend_demo.service.ChallengeParticipantService;
import com.starboost.starboost_backend_demo.service.ChallengeService;
import jakarta.transaction.Transactional;  
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional  
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {
    private final ChallengeRepository repo;
    private final ChallengeParticipantService participantService;

    @Override
    public List<ChallengeDto> findAll() {
        return repo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ChallengeDto findById(Long id) {
        Challenge c = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Challenge not found: " + id));
        return toDto(c);
    }

    @Override
    public ChallengeDto create(ChallengeDto dto) {
        
        Challenge entity = toEntity(dto);

        
        Challenge saved = repo.save(entity);

        
        participantService.enrollParticipants(
                saved.getId(),
                dto.getTargetRoles().stream().map(Enum::name).collect(Collectors.toSet())
        );

        
        return toDto(saved);
    }

    @Override
    public ChallengeDto update(Long id, ChallengeDto dto) {
        Challenge existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Challenge not found: " + id));

        
        existing.setName(dto.getName());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        existing.setTargetRoles(dto.getTargetRoles());
        existing.setTargetProducts(dto.getTargetProducts());
        existing.setStatus(dto.getStatus());
        existing.setDeleted(dto.isDeleted());

        
        existing.getRules().clear();
        if (dto.getRules() != null) {
            List<Rule> rules = dto.getRules().stream()
                    .map(rd -> Rule.builder()
                            .contractType(rd.getContractType())
                            .transactionNature(rd.getTransactionNature())
                            .packType(rd.getPackType())
                            .challenge(existing)
                            .build())
                    .collect(Collectors.toList());
            existing.getRules().addAll(rules);
        }


        
        existing.getWinningRules().clear();
        if (dto.getWinningRules() != null) {
            List<ChallengeWinningRule> wr = dto.getWinningRules().stream()
                    .map(wd -> ChallengeWinningRule.builder()
                            .challenge(existing)              
                            .roleCategory(wd.getRoleCategory())
                            .conditionType(wd.getConditionType())
                            .thresholdMin(wd.getThresholdMin())
                            .build())
                    .collect(Collectors.toList());
            existing.getWinningRules().addAll(wr);
        }

        
        existing.getRewardRules().clear();
        if (dto.getRewardRules() != null) {
            List<ChallengeRewardRule> rr = dto.getRewardRules().stream()
                    .map(rd -> ChallengeRewardRule.builder()
                            .challenge(existing)              
                            .roleCategory(rd.getRoleCategory())
                            .payoutType(rd.getPayoutType())
                            .tierMin(rd.getTierMin())
                            .tierMax(rd.getTierMax())
                            .baseAmount(rd.getBaseAmount())
                            .gift(rd.getGift())
                            .build())
                    .collect(Collectors.toList());
            existing.getRewardRules().addAll(rr);
        }

        
        Challenge saved = repo.save(existing);

        
        participantService.enrollParticipants(
                id,
                dto.getTargetRoles().stream().map(Enum::name).collect(Collectors.toSet())
        );

        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    

    private ChallengeDto toDto(Challenge c) {
        var ruleDtos = c.getRules().stream()
                .map(r -> RuleDto.builder()
                        .id(r.getId())
                        .contractType(r.getContractType())
                        .transactionNature(r.getTransactionNature())
                        .packType(r.getPackType())
                        .build())
                .collect(Collectors.toList());

        
        var scoreDtos = c.getScoreRules().stream()
                .map(sr -> ScoreRuleDto.builder()
                        .id(sr.getId())
                        .scoreType(sr.getScoreType())
                        .contractType(sr.getContractType())
                        .packType(sr.getPackType())
                        .points(sr.getPoints())
                        .revenueUnit(sr.getRevenueUnit())
                        .challengeId(c.getId())
                        .build())
                .collect(Collectors.toList());

        
        var winDtos = c.getWinningRules().stream()
                .map(w -> WinningRuleDto.builder()
                        .id(w.getId())
                        .roleCategory(w.getRoleCategory())
                        .conditionType(w.getConditionType())
                        .thresholdMin(w.getThresholdMin())
                        .build())
                .collect(Collectors.toList());

        var rewardDtos = c.getRewardRules().stream()
                .map(r -> RewardRuleDto.builder()
                        .id(r.getId())
                        .roleCategory(r.getRoleCategory())
                        .payoutType(r.getPayoutType())
                        .tierMin(r.getTierMin())
                        .tierMax(r.getTierMax())
                        .baseAmount(r.getBaseAmount())
                        .gift(r.getGift())
                        .build())
                .collect(Collectors.toList());

        return ChallengeDto.builder()
                .id(c.getId())
                .name(c.getName())
                .startDate(c.getStartDate())
                .endDate(c.getEndDate())
                .targetRoles(c.getTargetRoles())
                .targetProducts(c.getTargetProducts())
                .rules(ruleDtos)
                .winningRules(winDtos)
                .rewardRules(rewardDtos)
                
                .status(c.getEndDate().isBefore(LocalDate.now())
                        ? ChallengeStatus.ENDED
                        : ChallengeStatus.ONGOING)
                .deleted(c.isDeleted())
                .build();
    }

    private Challenge toEntity(ChallengeDto d) {
        Challenge chall = Challenge.builder()
                .name(d.getName())
                .startDate(d.getStartDate())
                .endDate(d.getEndDate())
                .targetRoles(d.getTargetRoles())
                .targetProducts(d.getTargetProducts())
                .status(d.getStatus())
                .deleted(d.isDeleted())
                .build();

        if (d.getRules() != null) {
            var rules = d.getRules().stream()
                    .map(rd -> Rule.builder()
                            .contractType(rd.getContractType())
                            .transactionNature(rd.getTransactionNature())
                            .packType(rd.getPackType())
                            .challenge(chall)
                            .build())
                    .collect(Collectors.toList());
            chall.setRules(rules);
        }



        if (d.getWinningRules() != null) {
            var wr = d.getWinningRules().stream()
                    .map(wd -> ChallengeWinningRule.builder()
                            .challenge(chall)
                            .roleCategory(wd.getRoleCategory())
                            .conditionType(wd.getConditionType())
                            .thresholdMin(wd.getThresholdMin())
                            .build())
                    .collect(Collectors.toList());
            chall.setWinningRules(wr);
        }

        if (d.getRewardRules() != null) {
            var rr = d.getRewardRules().stream()
                    .map(rd -> ChallengeRewardRule.builder()
                            .challenge(chall)
                            .roleCategory(rd.getRoleCategory())
                            .payoutType(rd.getPayoutType())
                            .tierMin(rd.getTierMin())
                            .tierMax(rd.getTierMax())
                            .baseAmount(rd.getBaseAmount())
                            .gift(rd.getGift())
                            .build())
                    .collect(Collectors.toList());
            chall.setRewardRules(rr);
        }

        return chall;
    }
}
