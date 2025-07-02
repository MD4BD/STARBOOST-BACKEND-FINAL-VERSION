package com.starboost.starboost_backend_demo.repository;

import com.starboost.starboost_backend_demo.entity.ChallengeParticipant;
import com.starboost.starboost_backend_demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, Long> {

    
    List<ChallengeParticipant> findAllByChallenge_Id(Long challengeId);

    
    List<ChallengeParticipant> findAllByChallenge_IdAndRole(Long challengeId, Role role);

    
    Optional<ChallengeParticipant> findByUserId(Long userId);

    
    long countByChallenge_IdAndAgencyIdAndRole(
            Long challengeId, Long agencyId, Role role);

    
    long countByChallenge_IdAndRegionIdAndRole(
            Long challengeId, Long regionId, Role role);

    
    long countByChallenge_IdAndAgencyId(Long challengeId, Long agencyId);
    
    long countByChallenge_IdAndRegionId(Long challengeId, Long regionId);
    
    void deleteAllByChallenge_Id(Long challengeId);
    
    Optional<ChallengeParticipant> findByChallenge_IdAndUserId(Long challengeId, Long userId);

    List<ChallengeParticipant> findAllByUser_Id(Long userId);

}
