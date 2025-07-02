package com.starboost.starboost_backend_demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    @Email @NotBlank
    private String email;

    @NotBlank
    private String redirectUrl;
}
