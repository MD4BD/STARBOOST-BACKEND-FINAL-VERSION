package com.starboost.starboost_backend_demo.dto;

import com.starboost.starboost_backend_demo.entity.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PerformanceDto {
    private Long participantId;   
    private Long challengeId;
    private Long userId;          
    private String name;          
    private Role role;
    private Long agencyId;
    private String agencyName;
    private Long regionId;
    private String regionName;

    private long totalContracts;
    private double totalRevenue;
    private int totalScore;
    private int rank;
}