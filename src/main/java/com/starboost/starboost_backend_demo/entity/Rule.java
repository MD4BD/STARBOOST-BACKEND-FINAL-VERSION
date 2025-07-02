package com.starboost.starboost_backend_demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Table(name = "challenge_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = "challenge")
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    ContractType contractType;

    @Enumerated(EnumType.STRING)
    TransactionNature transactionNature;

    @Enumerated(EnumType.STRING)
    PackType packType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    Challenge challenge;
}
