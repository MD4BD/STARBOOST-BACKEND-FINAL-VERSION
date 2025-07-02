package com.starboost.starboost_backend_demo.service;

import com.starboost.starboost_backend_demo.dto.PerformanceDto;

import java.util.List;


public interface ChallengePerformanceService {

    
    List<PerformanceDto> agents(Long challengeId,
                                Long filterUserId,
                                String filterName);
    
    List<PerformanceDto> commercials(Long challengeId,
                                     Long filterUserId,
                                     String filterName);
    
    List<PerformanceDto> agencies(Long challengeId,
                                  Long filterAgencyId,
                                  String filterName);
    
    List<PerformanceDto> regions(Long challengeId,
                                 Long filterRegionId,
                                 String filterName);

        
    long getContractCount(Long challengeId, Long userId);

    
    double getRevenue(Long challengeId, Long userId);

    
    int getScore(Long challengeId, Long userId);

    
    double getAgencyTotalRevenue(Long challengeId, Long agencyId);

    
    int getAgencyTotalScore(Long challengeId, Long agencyId);

    
    long getAgencyTotalContracts(Long challengeId, Long agencyId);

    
    double getRegionTotalRevenue(Long challengeId, Long regionId);

    
    int getRegionTotalScore(Long challengeId, Long regionId);

    
    long getRegionTotalContracts(Long challengeId, Long regionId);


}