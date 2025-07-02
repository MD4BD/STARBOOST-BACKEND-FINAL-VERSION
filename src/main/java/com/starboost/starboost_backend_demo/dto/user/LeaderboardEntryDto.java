package com.starboost.starboost_backend_demo.dto.user;

import lombok.Data;

@Data
public class LeaderboardEntryDto {
    private int rank;
    private Long userId;
    private String fullName;
    private String agencyOrRegion; 
    private double metricValue;   
}
