package com.starboost.starboost_backend_demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgencyDto {
    private Long id;

    @NotBlank(message = "Agency code is required")
    private String code;

    @NotBlank(message = "Agency name is required")
    private String name;

    @NotNull(message = "Region ID is required")
    private Long regionId;
}
