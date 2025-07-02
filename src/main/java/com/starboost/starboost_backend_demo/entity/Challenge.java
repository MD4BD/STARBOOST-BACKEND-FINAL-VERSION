package com.starboost.starboost_backend_demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.*;


@Entity
@Table(name = "challenges")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = {"rules", "winningRules", "rewardRules", "participants", "scoreRules"})
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    LocalDate startDate;

    @Column(nullable = false)
    LocalDate endDate;

    
    @ElementCollection(targetClass = Role.class)
    @CollectionTable(
            name        = "challenge_target_roles",
            joinColumns = @JoinColumn(name = "challenge_id")
    )
    @Enumerated(EnumType.STRING)
    @Builder.Default
    Set<Role> targetRoles = new HashSet<>();

    
    @ElementCollection(targetClass = Product.class)
    @CollectionTable(
            name        = "challenge_products",
            joinColumns = @JoinColumn(name = "challenge_id")
    )
    @Enumerated(EnumType.STRING)
    @Builder.Default
    Set<Product> targetProducts = new HashSet<>();

    
    @OneToMany(
            mappedBy      = "challenge",
            cascade       = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    List<Rule> rules = new ArrayList<>();

    
    @OneToMany(
            mappedBy      = "challenge",
            cascade       = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    List<ChallengeWinningRule> winningRules = new ArrayList<>();

    
    @OneToMany(
            mappedBy      = "challenge",
            cascade       = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    List<ChallengeRewardRule> rewardRules = new ArrayList<>();

    
    @OneToMany(
            mappedBy      = "challenge",
            cascade       = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    List<ChallengeParticipant> participants = new ArrayList<>();

    
    @OneToMany(
            mappedBy      = "challenge",
            cascade       = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    List<ScoreRule> scoreRules = new ArrayList<>();

    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    ChallengeStatus status = ChallengeStatus.ONGOING;

    @Builder.Default
    boolean deleted = false;
}
