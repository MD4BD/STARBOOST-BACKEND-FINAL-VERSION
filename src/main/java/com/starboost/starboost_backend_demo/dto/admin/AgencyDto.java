package com.starboost.starboost_backend_demo.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AgencyDto {
    private Long id;
    @NotBlank private String code;
    @NotBlank private String name;
    @NotNull private Long regionId;
}
