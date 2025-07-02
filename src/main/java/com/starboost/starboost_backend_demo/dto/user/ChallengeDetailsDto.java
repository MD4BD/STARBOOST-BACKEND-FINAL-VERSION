package com.starboost.starboost_backend_demo.dto.user;

import com.starboost.starboost_backend_demo.dto.RewardRuleDto;
import com.starboost.starboost_backend_demo.dto.RuleDto;
import com.starboost.starboost_backend_demo.dto.WinningRuleDto;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class ChallengeDetailsDto {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    private List<String> targetRoles;
    private List<String> targetProducts;

    private List<RuleDto> rules;             
    private List<WinningRuleDto> winningRules; 
    private List<RewardRuleDto> rewardRules;   
}
