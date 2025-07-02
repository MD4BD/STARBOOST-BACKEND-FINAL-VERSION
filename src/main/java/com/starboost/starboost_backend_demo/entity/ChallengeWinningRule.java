package com.starboost.starboost_backend_demo.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "challenge_winning_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeWinningRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    
    @Enumerated(EnumType.STRING)
    @Column(name = "role_category", nullable = false)
    private Role roleCategory;

    
    @Column(name = "threshold_min", nullable = false)
    private Double thresholdMin;


    
    @Enumerated(EnumType.STRING)
    @Column(name = "condition_type", nullable = false)
    private ConditionType conditionType;

    
    @Lob
    @Column(name = "formula_details")
    private String formulaDetails;
}
