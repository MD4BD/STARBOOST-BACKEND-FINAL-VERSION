package com.starboost.starboost_backend_demo.dto.admin;

import lombok.Data;

@Data
public class LeaderboardDto {
    private Long userId;
    private String name;
    private int score;
}
