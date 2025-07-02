package com.starboost.starboost_backend_demo.repository;

import com.starboost.starboost_backend_demo.entity.ChallengeRewardRule;
import com.starboost.starboost_backend_demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChallengeRewardRuleRepository
        extends JpaRepository<ChallengeRewardRule, Long> {

    List<ChallengeRewardRule> findAllByChallenge_IdAndRoleCategory(
            Long challengeId,
            Role roleCategory
    );

    List<ChallengeRewardRule> findAllByChallenge_Id(Long challengeId);
}
