package com.starboost.starboost_backend_demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChallengeLeaderboardDto {
    private int    rank;
    private Long   userId;       
    private String name;         
    private String role;         
    private Long   agencyId;     
    private String agencyName;
    private Long   regionId;     
    private String regionName;
    private int    score;
}
