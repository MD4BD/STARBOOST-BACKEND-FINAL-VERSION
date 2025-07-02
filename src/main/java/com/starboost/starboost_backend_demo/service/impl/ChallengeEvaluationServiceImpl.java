package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.RewardRuleDto;
import com.starboost.starboost_backend_demo.dto.WinnerDto;
import com.starboost.starboost_backend_demo.entity.*;
import com.starboost.starboost_backend_demo.repository.AgencyRepository;
import com.starboost.starboost_backend_demo.repository.ChallengeWinningRuleRepository;
import com.starboost.starboost_backend_demo.repository.RegionRepository;
import com.starboost.starboost_backend_demo.repository.UserRepository;
import com.starboost.starboost_backend_demo.service.ChallengeEvaluationService;
import com.starboost.starboost_backend_demo.service.ChallengeParticipantService;
import com.starboost.starboost_backend_demo.service.ChallengePerformanceService;
import com.starboost.starboost_backend_demo.service.ChallengeRewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class ChallengeEvaluationServiceImpl implements ChallengeEvaluationService {
    private final ChallengeWinningRuleRepository winningRuleRepo;
    private final ChallengeRewardService          rewardService;
    private final ChallengeParticipantService     participantService;
    private final ChallengePerformanceService     performanceService;
    private final UserRepository                  userRepo;
    private final AgencyRepository                agencyRepo;
    private final RegionRepository                regionRepo;

    @Override
    public List<WinnerDto> listWinners(Long challengeId) {
        
        return Stream.of(Role.values())
                .flatMap(role -> listWinnersByRole(challengeId, role).stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<WinnerDto> listWinnersByRole(Long challengeId, Role role) {
        
        List<ChallengeWinningRule> winRules =
                winningRuleRepo.findAllByChallenge_IdAndRoleCategory(challengeId, role);
        System.out.println("DEBUG [listWinnersByRole] Winning rules for " + role + ": " + winRules);

        
        List<Long> userIds = participantService.listParticipantIds(challengeId, role);
        System.out.println("DEBUG [listWinnersByRole] Participants for " + role + ": " + userIds);

        
        System.out.println("DEBUG [listWinnersByRole] Raw metrics:");
        for (Long uid : userIds) {
            System.out.println("  user " + uid
                    + " → contracts=" + performanceService.getContractCount(challengeId, uid)
                    + ", revenue="   + performanceService.getRevenue(challengeId, uid)
                    + ", score="     + performanceService.getScore(challengeId, uid));
        }

        
        List<Candidate> candidates = userIds.stream()
                .map(id -> assembleCandidate(challengeId, id, winRules))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        System.out.println("DEBUG [listWinnersByRole] All assembled candidates:");
        for (Candidate c : candidates) {
            System.out.println("  candidate user=" + c.userId
                    + ", contractCount=" + c.contractCount
                    + ", revenue="       + c.revenue
                    + ", score="         + c.score
                    + ", avg="           + c.average
                    + ", wAvg="          + c.weightedAverage);
        }

        System.out.println("DEBUG [listWinnersByRole] Passing eligibility:");
        candidates.forEach(c -> System.out.println("  userId=" + c.userId));

        
        candidates.sort(getComparator(winRules).reversed());

        
        List<WinnerDto> winners = new ArrayList<>();
        for (int i = 0; i < candidates.size(); i++) {
            Candidate c = candidates.get(i);
            int rank = i + 1;

            
            List<RewardRuleDto> rewardRules = rewardService.listRewardRules(challengeId, role);
            if (rewardRules == null || rewardRules.isEmpty()) {
                continue;  
            }
            PayoutType payoutType = rewardRules.get(0).getPayoutType();

            
            long unitCount;
            double metricValue;
            switch (payoutType) {
                case RANK_TIERS:
                    unitCount   = 1;
                    metricValue = rank;
                    break;
                case PERCENT_TIERS:
                    unitCount   = 1;
                    metricValue = c.revenue;
                    break;
                case FIXED_TIERS:
                default:
                    switch (role) {
                        case COMMERCIAL:
                            unitCount   = 1;
                            metricValue = c.revenue;
                            break;
                        case AGENCY_MANAGER:
                            unitCount   = participantService.countCommercialsInAgency(challengeId, c.agencyId);
                            metricValue = performanceService.getAgencyTotalRevenue(challengeId, c.agencyId);
                            break;
                        case REGIONAL_MANAGER:
                        case ANIMATOR:
                            unitCount   = participantService.countSalesPointsInRegion(challengeId, c.regionId);
                            metricValue = performanceService.getRegionTotalRevenue(challengeId, c.regionId);
                            break;
                        case AGENT:
                        default:
                            unitCount   = 1;
                            metricValue = c.revenue;
                    }
            }

            
            double payout = rewardService.computeReward(challengeId, role, unitCount, metricValue);
            System.out.println("DEBUG [listWinnersByRole] payout for user="
                    + c.userId + " → unitCount=" + unitCount
                    + ", metric=" + metricValue
                    + ", payout=" + payout);

            
            String gift = rewardRules.stream()
                    .filter(rr -> rr.getPayoutType() == payoutType
                            && metricValue >= rr.getTierMin()
                            && metricValue <= rr.getTierMax()
                            && rr.getGift() != null)           
                    .map(RewardRuleDto::getGift)
                    .findFirst()
                    .orElse(null);

            if (payout <= 0) {
                continue;  
            }

            
            String regionName = null;
            if (c.regionId != null) {
                regionName = regionRepo.findById(c.regionId)
                        .map(Region::getName)
                        .orElse(null);
            }

            WinnerDto.WinnerDtoBuilder b = WinnerDto.builder()
                    .userId(c.userId)
                    .roleCategory(role)
                    .agencyId(c.agencyId)
                    .regionId(c.regionId)
                    .regionName(regionName)
                    .rank(rank)
                    .unitCount(unitCount)
                    .rewardAmount(payout)
                    .gift(gift);

            
            User u = userRepo.findById(c.userId).orElse(null);
            String fn = (u != null ? u.getFirstName() : null);
            String ln = (u != null ? u.getLastName()  : null);

            
            if (role == Role.COMMERCIAL) {
                String agencyName = null;
                if (c.agencyId != null) {
                    agencyName = agencyRepo.findById(c.agencyId)
                            .map(Agency::getName)
                            .orElse(null);
                }
                winners.add(b
                        .firstName(fn)
                        .lastName(ln)
                        .agencyName(agencyName)
                        .contractCount(c.contractCount)
                        .revenue(c.revenue)
                        .score(c.score)
                        .average(c.average  == null ? 0.0 : c.average)
                        .weightedAverage(c.weightedAverage == null ? 0.0 : c.weightedAverage)
                        .build());

            } else if (role == Role.AGENT) {
                winners.add(b
                        .firstName(fn)
                        .lastName(ln)
                        .agencyName(null)
                        .contractCount(c.contractCount)
                        .revenue(c.revenue)
                        .score(c.score)
                        .average(c.average  == null ? 0.0 : c.average)
                        .weightedAverage(c.weightedAverage == null ? 0.0 : c.weightedAverage)
                        .build());

            } else if (role == Role.AGENCY_MANAGER) {
                if (c.agencyId == null) continue;
                long tc = performanceService.getAgencyTotalContracts(challengeId, c.agencyId);
                double tr = performanceService.getAgencyTotalRevenue(challengeId, c.agencyId);
                double ts = performanceService.getAgencyTotalScore(challengeId, c.agencyId);
                String agencyName = agencyRepo.findById(c.agencyId)
                        .map(Agency::getName)
                        .orElse(null);

                winners.add(b
                        .firstName(fn)
                        .lastName(ln)
                        .agencyName(agencyName)
                        .contractCount(tc)
                        .revenue(tr)
                        .score(ts)
                        .average(c.average  == null ? 0.0 : c.average)
                        .weightedAverage(c.weightedAverage == null ? 0.0 : c.weightedAverage)
                        .build());

            } else if (role == Role.REGIONAL_MANAGER || role == Role.ANIMATOR) {
                if (c.regionId == null) continue;
                long tc = performanceService.getRegionTotalContracts(challengeId, c.regionId);
                double tr = performanceService.getRegionTotalRevenue(challengeId, c.regionId);
                double ts = performanceService.getRegionTotalScore(challengeId, c.regionId);

                winners.add(b
                        .firstName(fn)
                        .lastName(ln)
                        .agencyName(null)
                        .contractCount(tc)
                        .revenue(tr)
                        .score(ts)
                        .average(c.average  == null ? 0.0 : c.average)
                        .weightedAverage(c.weightedAverage == null ? 0.0 : c.weightedAverage)
                        .build());
            }
        }

        return winners;
    }


    

    
    private Candidate assembleCandidate(Long challengeId, Long userId, List<ChallengeWinningRule> winRules) {
        Candidate c = new Candidate();
        c.userId   = userId;
        c.agencyId = participantService.getAgencyIdForUser(userId);
        c.regionId = participantService.getRegionIdForUser(userId);

        
        if (!winRules.isEmpty()) {
            Role r = winRules.get(0).getRoleCategory();
            switch (r) {
                case AGENCY_MANAGER:
                    c.contractCount = performanceService.getAgencyTotalContracts(challengeId, c.agencyId);
                    c.revenue       = performanceService.getAgencyTotalRevenue(challengeId, c.agencyId);
                    c.score         = performanceService.getAgencyTotalScore(challengeId, c.agencyId);
                    break;
                case REGIONAL_MANAGER:
                case ANIMATOR:
                    c.contractCount = performanceService.getRegionTotalContracts(challengeId, c.regionId);
                    c.revenue       = performanceService.getRegionTotalRevenue(challengeId, c.regionId);
                    c.score         = performanceService.getRegionTotalScore(challengeId, c.regionId);
                    break;
                default:
                    c.contractCount = performanceService.getContractCount(challengeId, userId);
                    c.revenue       = performanceService.getRevenue(challengeId, userId);
                    c.score         = performanceService.getScore(challengeId, userId);
            }
        } else {
            c.contractCount = performanceService.getContractCount(challengeId, userId);
            c.revenue       = performanceService.getRevenue(challengeId, userId);
            c.score         = performanceService.getScore(challengeId, userId);
        }

        
        if (winRules.stream().anyMatch(r -> r.getConditionType() == ConditionType.MIN_AVG_PER_COMMERCIAL)) {
            double tot = performanceService.getAgencyTotalRevenue(challengeId, c.agencyId);
            long cnt  = participantService.countCommercialsInAgency(challengeId, c.agencyId);
            c.average = (cnt > 0 ? tot / cnt : null);
        }
        if (winRules.stream().anyMatch(r -> r.getConditionType() == ConditionType.MIN_AVG_PER_PV)) {
            double tot = performanceService.getRegionTotalRevenue(challengeId, c.regionId);
            long cnt   = participantService.countSalesPointsInRegion(challengeId, c.regionId);
            c.average = (cnt > 0 ? tot / cnt : c.average);
        }

        if (winRules.stream().anyMatch(r -> r.getConditionType() == ConditionType.WEIGHTED_AVG_AGENCY)) {
            int totS  = performanceService.getAgencyTotalScore(challengeId, c.agencyId);
            long cnt  = participantService.countCommercialsInAgency(challengeId, c.agencyId);
            double w  = computeAgencyWeight(cnt);
            c.weightedAverage = (cnt > 0 ? (totS / (double)cnt) * w : null);
        }
        if (winRules.stream().anyMatch(r -> r.getConditionType() == ConditionType.WEIGHTED_AVG_REGION)) {
            int totS  = performanceService.getRegionTotalScore(challengeId, c.regionId);
            long cnt  = participantService.countSalesPointsInRegion(challengeId, c.regionId);
            double w  = computeRegionWeight(cnt);
            c.weightedAverage = (cnt > 0 ? (totS / (double)cnt) * w : c.weightedAverage);
        }

        return passesAllWinningRules(c, winRules) ? c : null;
    }

    private boolean passesAllWinningRules(Candidate c, List<ChallengeWinningRule> rules) {
        for (ChallengeWinningRule r : rules) {
            double min = r.getThresholdMin();
            switch (r.getConditionType()) {
                case MIN_CONTRACTS:
                    if (c.contractCount < min) return false; break;
                case MIN_REVENUE:
                    if (c.revenue < min) return false; break;
                case MIN_AVG_PER_COMMERCIAL:
                case MIN_AVG_PER_PV:
                    if (c.average == null || c.average < min) return false; break;
                case WEIGHTED_AVG_AGENCY:
                case WEIGHTED_AVG_REGION:
                    if (c.weightedAverage == null || c.weightedAverage < min) return false; break;
                default:
                    return false;
            }
        }
        return true;
    }

    private Comparator<Candidate> getComparator(List<ChallengeWinningRule> rules) {
        if (rules.stream().anyMatch(r ->
                r.getConditionType() == ConditionType.WEIGHTED_AVG_AGENCY
                        || r.getConditionType() == ConditionType.WEIGHTED_AVG_REGION)) {
            return Comparator.comparing(c -> c.weightedAverage == null ? 0.0 : c.weightedAverage);
        }
        if (rules.stream().anyMatch(r ->
                r.getConditionType() == ConditionType.MIN_AVG_PER_COMMERCIAL
                        || r.getConditionType() == ConditionType.MIN_AVG_PER_PV)) {
            return Comparator.comparing(c -> c.average == null ? 0.0 : c.average);
        }
        return Comparator.comparing(c -> c.score);
    }

    private double computeAgencyWeight(long commCount) {
        if (commCount >= 3) return 2.0;
        if (commCount == 2) return 1.5;
        return 1.0;
    }

    private double computeRegionWeight(long pvCount) {
        if (pvCount >= 26) return 2.0;
        if (pvCount >= 16) return 1.5;
        return 1.0;
    }

    
    private static class Candidate {
        Long   userId;
        Long   agencyId;
        Long   regionId;
        long   contractCount;
        double revenue;
        double score;
        Double average;
        Double weightedAverage;
    }
}
