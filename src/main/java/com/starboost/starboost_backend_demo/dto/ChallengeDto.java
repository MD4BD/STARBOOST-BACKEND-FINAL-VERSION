package com.starboost.starboost_backend_demo.dto;

import com.starboost.starboost_backend_demo.entity.ChallengeStatus;
import com.starboost.starboost_backend_demo.entity.Product;
import com.starboost.starboost_backend_demo.entity.Role;
import com.starboost.starboost_backend_demo.dto.RuleDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotEmpty(message = "At least one target role is required")
    private Set<@NotNull Role> targetRoles;

    @NotEmpty(message = "At least one product is required")
    private Set<@NotNull Product> targetProducts;

    @NotEmpty(message = "Filter rules cannot be empty")
    @Valid
    private List<RuleDto> rules;                  

    @NotEmpty(message = "Winning rules cannot be empty")
    @Valid
    private List<WinningRuleDto> winningRules;

    @NotEmpty(message = "Reward rules cannot be empty")
    @Valid
    private List<RewardRuleDto> rewardRules;

    @NotNull(message = "Status is required")
    private ChallengeStatus status;

    private boolean deleted;
}
