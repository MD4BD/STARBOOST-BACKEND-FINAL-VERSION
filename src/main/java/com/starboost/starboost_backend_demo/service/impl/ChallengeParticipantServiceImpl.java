package com.starboost.starboost_backend_demo.service.impl;

import com.starboost.starboost_backend_demo.dto.ChallengeParticipantDto;
import com.starboost.starboost_backend_demo.entity.*;
import com.starboost.starboost_backend_demo.repository.ChallengeParticipantRepository;
import com.starboost.starboost_backend_demo.repository.ChallengeRepository;
import com.starboost.starboost_backend_demo.repository.UserRepository;
import com.starboost.starboost_backend_demo.repository.AgencyRepository;
import com.starboost.starboost_backend_demo.service.ChallengeParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChallengeParticipantServiceImpl implements ChallengeParticipantService {
    private final ChallengeParticipantRepository participantRepo;
    private final ChallengeRepository           challengeRepo;
    private final UserRepository                userRepo;
    private final AgencyRepository           agencyRepo;
    private final ChallengeParticipantRepository repo;

    
    @Override
    public List<ChallengeParticipantDto> enrollParticipants(Long challengeId, Set<String> targetRoles) {
        
        participantRepo.deleteAllByChallenge_Id(challengeId);

        
        Challenge challenge = challengeRepo.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("Challenge not found: " + challengeId));

        
        List<ChallengeParticipant> participants = userRepo.findAll().stream()
                .filter(u -> targetRoles.contains(u.getRole().name()))
                .map(u -> ChallengeParticipant.builder()
                        .challenge(challenge)
                        .user(u)
                        .role(u.getRole())
                        .agencyId(u.getAgency() != null ? u.getAgency().getId() : null)
                        .regionId(u.getRegion() != null ? u.getRegion().getId() : null)
                        .status(ParticipantStatus.ACTIVE)
                        .build())
                .collect(Collectors.toList());

        
        participantRepo.saveAll(participants);
        return participants.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    
    @Override
    public List<ChallengeParticipantDto> findByChallengeId(Long challengeId) {
        return participantRepo.findAllByChallenge_Id(challengeId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    
    @Override
    public ChallengeParticipantDto findByChallengeAndUser(Long challengeId, Long userId) {
        ChallengeParticipant p = participantRepo
                .findByChallenge_IdAndUserId(challengeId, userId)
                .orElseThrow(() -> new RuntimeException(
                        "Participant not found for challenge=" + challengeId + " user=" + userId));
        return toDto(p);
    }

    
    @Override
    public ChallengeParticipantDto findByParticipantId(Long participantId) {
        ChallengeParticipant p = participantRepo.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant not found: " + participantId));
        return toDto(p);
    }

    
    @Override
    public List<ChallengeParticipantDto> findByChallengeAndRole(
            Long challengeId,
            Role role,
            Long filterId,
            String filterName
    ) {
        return findByChallengeId(challengeId).stream()
                .filter(dto -> dto.getRole() == role)
                .filter(dto -> filterId == null
                        || dto.getParticipantId().equals(filterId)
                        || dto.getUserId().equals(filterId))
                .filter(dto -> filterName == null
                        || (dto.getFirstName() + " " + dto.getLastName())
                        .toLowerCase().contains(filterName.toLowerCase()))
                .collect(Collectors.toList());
    }

    
    @Override
    public List<Long> listParticipantIds(Long challengeId, Role roleCategory) {
        return findByChallengeAndRole(challengeId, roleCategory, null, null).stream()
                .map(ChallengeParticipantDto::getUserId)
                .collect(Collectors.toList());
    }

    
    @Override
    public Long getAgencyIdForUser(Long userId) {
        return userRepo.findById(userId)
                .map(u -> u.getAgency() != null ? u.getAgency().getId() : null)
                .orElse(null);
    }

    
    @Override
    public Long getRegionIdForUser(Long userId) {
        return userRepo.findById(userId)
                .map(u -> u.getRegion() != null ? u.getRegion().getId() : null)
                .orElse(null);
    }

    
    @Override
    public long countCommercialsInAgency(Long challengeId, Long agencyId) {
        
        return participantRepo.countByChallenge_IdAndAgencyIdAndRole(
                challengeId, agencyId, Role.COMMERCIAL
        );
    }



     
     @Override
     public long countSalesPointsInRegion(Long challengeId, Long regionId) {
         
         long agents = participantRepo
                 .countByChallenge_IdAndRegionIdAndRole(
                         challengeId,
                         regionId,
                         Role.AGENT
                 );

         
         long agencies = agencyRepo.countByRegionId(regionId);

         return agents + agencies;
     }


    @Override
    public List<Long> listChallengeIdsForUser(Long userId) {
        return repo.findAllByUser_Id(userId)
                .stream()
                .map(cp -> cp.getChallenge().getId())
                .toList();
    }

    @Override
    public Long getUserIdByEmail(String email) {
        return userRepo
                .findByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }



    
    private ChallengeParticipantDto toDto(ChallengeParticipant p) {
        return ChallengeParticipantDto.builder()
                .participantId(p.getId())
                .challengeId(p.getChallenge().getId())
                .userId(p.getUser().getId())
                .firstName(p.getUser().getFirstName())
                .lastName(p.getUser().getLastName())
                .role(p.getRole())
                .agencyId(p.getAgencyId())
                .regionId(p.getRegionId())
                .status(p.getStatus())
                .build();
    }
}
