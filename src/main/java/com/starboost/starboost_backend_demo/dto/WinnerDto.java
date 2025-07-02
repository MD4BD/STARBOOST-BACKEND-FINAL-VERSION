package com.starboost.starboost_backend_demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.starboost.starboost_backend_demo.entity.Role;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WinnerDto {
    
    private Long userId;

    
    private Role roleCategory;

    private String firstName;

    
    private String lastName;

    

    
    private Long agencyId;

    private String agencyName;

    
    private Long regionId;

    private String  regionName;

    
    private int rank;

    
    private long contractCount;
    private double revenue;
    private double score;

    
    private Double average;

    
    private Double weightedAverage;

    
    private Long    unitCount;

    
    private double rewardAmount;

    private String gift;
}
