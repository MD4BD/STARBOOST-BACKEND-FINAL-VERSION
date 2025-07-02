package com.starboost.starboost_backend_demo.dto.admin;

import com.starboost.starboost_backend_demo.entity.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;


@Data
public class ParticipantCreateDto {
    @NotEmpty(message = "At least one user ID is required")
    private Set<@NotNull Long> userIds;

    @NotNull(message = "Role is required")
    private Role role;
}
