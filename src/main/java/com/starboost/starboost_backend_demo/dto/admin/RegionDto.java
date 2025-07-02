package com.starboost.starboost_backend_demo.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegionDto {
    private Long id;
    @NotBlank private String code;
    @NotBlank private String name;
}
