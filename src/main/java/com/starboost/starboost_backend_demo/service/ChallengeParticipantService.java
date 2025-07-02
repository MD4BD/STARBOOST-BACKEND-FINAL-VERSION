package com.starboost.starboost_backend_demo.service;

import com.starboost.starboost_backend_demo.dto.ChallengeParticipantDto;
import com.starboost.starboost_backend_demo.entity.Role;

import java.util.List;
import java.util.Set;


public interface ChallengeParticipantService {
    List<ChallengeParticipantDto> enrollParticipants(Long challengeId, Set<String> targetRoles);
    List<ChallengeParticipantDto> findByChallengeId(Long challengeId);
    ChallengeParticipantDto findByChallengeAndUser(Long challengeId, Long userId);
    ChallengeParticipantDto findByParticipantId(Long participantId);
    List<ChallengeParticipantDto> findByChallengeAndRole(
            Long challengeId,
            Role role,
            Long filterId,
            String filterName
    );

    Long getUserIdByEmail(String email);
    List<Long> listChallengeIdsForUser(Long userId);

    List<Long> listParticipantIds(Long challengeId, Role roleCategory);

    Long getAgencyIdForUser(Long userId);

    Long getRegionIdForUser(Long userId);

    long countCommercialsInAgency(Long challengeId, Long agencyId);

    long countSalesPointsInRegion(Long challengeId, Long regionId);
}
