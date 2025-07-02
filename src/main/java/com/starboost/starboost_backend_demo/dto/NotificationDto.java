package com.starboost.starboost_backend_demo.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private Long id;

    private String title;

    private String message;
    private String targetType;
    private String targetValue;
    private LocalDateTime createdAt;
    private LocalDateTime sendAt;

X    private boolean sent;

    private boolean read;
}
