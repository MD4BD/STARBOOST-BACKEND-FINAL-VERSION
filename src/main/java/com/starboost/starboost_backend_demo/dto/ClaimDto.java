package com.starboost.starboost_backend_demo.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimDto {
    private Long id;
    private String title;
    private String message;

    private LocalDateTime createdAt;

    private boolean sent;

    private boolean read;

X    private Long userId;
}
