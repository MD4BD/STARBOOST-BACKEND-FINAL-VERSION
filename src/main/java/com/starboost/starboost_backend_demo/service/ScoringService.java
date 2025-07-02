package com.starboost.starboost_backend_demo.service;

import java.util.Map;

public interface ScoringService {

    Map<Long, Integer> calculateScores(Long challengeId);
}
