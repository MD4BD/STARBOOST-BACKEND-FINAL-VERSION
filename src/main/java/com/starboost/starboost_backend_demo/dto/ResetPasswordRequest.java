package com.starboost.starboost_backend_demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotBlank
    private String token;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;
}
