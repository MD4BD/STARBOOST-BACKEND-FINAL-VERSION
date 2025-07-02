package com.starboost.starboost_backend_demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "notification_read",
        uniqueConstraints = @UniqueConstraint(columnNames = {"notification_id","user_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRead {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="notification_id", nullable=false)
    private Long notificationId;

    @Column(name="user_id", nullable=false)
    private Long userId;

    @Column(name="read_at", nullable=false)
    private LocalDateTime readAt;
}
