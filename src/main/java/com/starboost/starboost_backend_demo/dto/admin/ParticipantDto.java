package com.starboost.starboost_backend_demo.dto.admin;

import lombok.Data;

@Data
public class ParticipantDto {
    private Long participantId;
    private Long userId;
    private String role;
}
