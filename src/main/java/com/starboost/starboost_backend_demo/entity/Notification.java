package com.starboost.starboost_backend_demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private String targetType;

    @Column(nullable = false)
    private String targetValue;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    
    @Column(nullable = false)
    private LocalDateTime sendAt;

    
    @Column(nullable = false)
    private boolean sent;
}
