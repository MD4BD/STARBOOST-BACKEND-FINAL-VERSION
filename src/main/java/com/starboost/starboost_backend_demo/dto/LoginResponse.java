package com.starboost.starboost_backend_demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data @AllArgsConstructor
public class LoginResponse {
    private String token;
}