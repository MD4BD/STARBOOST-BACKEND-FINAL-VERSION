package com.starboost.starboost_backend_demo.dto.admin;

import lombok.Data;

@Data
public class PerformanceDto {
    private Long userId;
    private String name;
    private long totalContracts;
    private double totalRevenue;
    private int totalScore;
}
