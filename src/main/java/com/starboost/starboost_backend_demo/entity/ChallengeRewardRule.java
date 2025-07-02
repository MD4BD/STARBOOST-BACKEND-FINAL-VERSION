package com.starboost.starboost_backend_demo.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "challenge_reward_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeRewardRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_category", nullable = false)
    private Role roleCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "payout_type", nullable = false)
    private PayoutType payoutType;   

    @Column(name = "tier_min", nullable = false)
    private Double tierMin;  

    @Column(name = "tier_max", nullable = false)
    private Double tierMax;   

    @Column(name = "base_amount", nullable = false)
    private Double baseAmount;

    @Column(name = "gift", nullable = true)
    private String gift;
}

