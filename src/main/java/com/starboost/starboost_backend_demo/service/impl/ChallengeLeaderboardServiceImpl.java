package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.ChallengeLeaderboardDto;
import com.starboost.starboost_backend_demo.dto.ChallengeParticipantDto;
import com.starboost.starboost_backend_demo.entity.Agency;
import com.starboost.starboost_backend_demo.entity.Region;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.repository.AgencyRepository;
import com.starboost.starboost_backend_demo.repository.RegionRepository;
import com.starboost.starboost_backend_demo.service.ChallengeLeaderboardService;
import com.starboost.starboost_backend_demo.service.ChallengeParticipantService;
import com.starboost.starboost_backend_demo.service.ScoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeLeaderboardServiceImpl implements ChallengeLeaderboardService {
    private final ChallengeParticipantService participantService;
    private final ScoringService              scoringService;
    private final AgencyRepository             agencyRepo;
    private final RegionRepository             regionRepo;

    
    private Map<Long,String> loadRegionNames(Collection<Long> regionIds) {
        if (regionIds.isEmpty()) return Map.of();
        return regionRepo.findAllById(regionIds).stream()
                .collect(Collectors.toMap(Region::getId, Region::getName));
    }

    
    private Map<Long,String> loadAgencyNames(Collection<Long> agencyIds) {
        if (agencyIds.isEmpty()) return Map.of();
        return agencyRepo.findAllById(agencyIds).stream()
                .collect(Collectors.toMap(Agency::getId, Agency::getName));
    }



    @Override
    public List<ChallengeLeaderboardDto> agents(Long challengeId) {
        
        List<ChallengeParticipantDto> parts = participantService.findByChallengeId(challengeId).stream()
                .filter(p -> p.getRole() == Role.AGENT)
                .collect(Collectors.toList());  

        
        Map<Long,Integer> scores = scoringService.calculateScores(challengeId);

        
        Set<Long> regionIds = parts.stream()
                .map(ChallengeParticipantDto::getRegionId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long,String> regionNames = loadRegionNames(regionIds);

        
        List<ChallengeLeaderboardDto> list = parts.stream()
                .map(p -> ChallengeLeaderboardDto.builder()
                        .userId(p.getUserId())
                        .name(p.getFirstName() + " " + p.getLastName())
                        .role("AGENT")
                        .agencyId(p.getAgencyId())
                        .regionId(p.getRegionId())
                        .regionName(regionNames.get(p.getRegionId()))  
                        .score(scores.getOrDefault(p.getUserId(), 0))
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));  

        
        return assignRanks(list);
    }


    @Override
    public List<ChallengeLeaderboardDto> commercials(Long challengeId) {
        
        List<ChallengeParticipantDto> parts = participantService.findByChallengeId(challengeId).stream()
                .filter(p -> p.getRole() == Role.COMMERCIAL)
                .collect(Collectors.toList());  

        
        Map<Long, Integer> scores = scoringService.calculateScores(challengeId);

        
        Set<Long> regionIds = parts.stream()
                .map(ChallengeParticipantDto::getRegionId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, String> regionNames = loadRegionNames(regionIds);

        
        Set<Long> agencyIds = parts.stream()
                .map(ChallengeParticipantDto::getAgencyId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, String> agencyNames = loadAgencyNames(agencyIds);

        
        List<ChallengeLeaderboardDto> list = parts.stream()
                .map(p -> ChallengeLeaderboardDto.builder()
                        .userId(p.getUserId())
                        .name(p.getFirstName() + " " + p.getLastName())
                        .role("COMMERCIAL")
                        .agencyId(p.getAgencyId())
                        .agencyName(agencyNames.get(p.getAgencyId()))   
                        .regionId(p.getRegionId())
                        .regionName(regionNames.get(p.getRegionId()))   
                        .score(scores.getOrDefault(p.getUserId(), 0))
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));    

        
        return assignRanks(list);
    }


    @Override
    public List<ChallengeLeaderboardDto> agencies(Long challengeId) {
        
        List<ChallengeParticipantDto> parts = participantService.findByChallengeId(challengeId)
                .stream()
                .filter(p -> p.getRole() == Role.COMMERCIAL && p.getAgencyId() != null)
                .collect(Collectors.toList());

        
        Map<Long, Integer> scores = scoringService.calculateScores(challengeId);

        
        Set<Long> agencyIds = parts.stream()
                .map(ChallengeParticipantDto::getAgencyId)
                .collect(Collectors.toSet());
        Map<Long, Agency> agencies = agencyRepo.findAllById(agencyIds).stream()
                .collect(Collectors.toMap(Agency::getId, ag -> ag));

        
        Set<Long> regionIds = agencies.values().stream()
                .map(Agency::getRegion)
                .map(Region::getId)
                .collect(Collectors.toSet());
        Map<Long, String> regionNames = loadRegionNames(regionIds);

        
        List<ChallengeLeaderboardDto> list = new ArrayList<>();
        for (var entry : parts.stream().collect(Collectors.groupingBy(ChallengeParticipantDto::getAgencyId)).entrySet()) {
            Long aid = entry.getKey();
            int totalScore = entry.getValue().stream()
                    .mapToInt(p -> scores.getOrDefault(p.getUserId(), 0))
                    .sum();

            Agency ag = agencies.get(aid);
            Long rid = ag.getRegion().getId();

            list.add(ChallengeLeaderboardDto.builder()
                    
                    .userId(null)
                    
                    .name(ag.getName())
                    .role("AGENCY")
                    .agencyId(aid)
                    .regionId(rid)
                    
                    .regionName(regionNames.get(rid))
                    .score(totalScore)
                    .build());
        }

        
        return assignRanks(list);
    }

    @Override
    public List<ChallengeLeaderboardDto> regions(Long challengeId) {
        Map<Long, Integer> scores = scoringService.calculateScores(challengeId);
        Map<Long, List<ChallengeParticipantDto>> byRegion = participantService.findByChallengeId(challengeId).stream()
                .filter(p -> p.getRegionId() != null)
                .collect(Collectors.groupingBy(ChallengeParticipantDto::getRegionId));

        List<ChallengeLeaderboardDto> list = new ArrayList<>();
        for (var entry : byRegion.entrySet()) {
            Long rid = entry.getKey();
            int totalScore = entry.getValue().stream()
                    .mapToInt(p -> scores.getOrDefault(p.getUserId(), 0))
                    .sum();
            Region region = regionRepo.findById(rid).orElseThrow();
            list.add(ChallengeLeaderboardDto.builder()
                    .rank(0)
                    .userId(null)
                    .name(region.getName())
                    .role("REGION")
                    .agencyId(null)
                    .regionId(rid)
                    .score(totalScore)
                    .build());
        }
        return assignRanks(list);
    }

    
    private List<ChallengeLeaderboardDto> assignRanks(List<ChallengeLeaderboardDto> list) {
        Collections.sort(list, Comparator.comparingInt(ChallengeLeaderboardDto::getScore).reversed());
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setRank(i + 1);
        }
        return list;
    }
}
