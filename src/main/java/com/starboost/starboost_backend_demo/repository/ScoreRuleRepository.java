package com.starboost.starboost_backend_demo.repository;

import com.starboost.starboost_backend_demo.entity.ScoreRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ScoreRuleRepository extends JpaRepository<ScoreRule, Long> {

    List<ScoreRule> findAllByChallenge_Id(Long challengeId);
}