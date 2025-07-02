package com.starboost.starboost_backend_demo.dto;

import lombok.*;
import com.starboost.starboost_backend_demo.entity.ConditionType;
import com.starboost.starboost_backend_demo.entity.Role;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WinningRuleDto {
    private Long id;
    private Role roleCategory;


    private ConditionType conditionType;

    private Double thresholdMin;

    private String formulaDetails;
}
