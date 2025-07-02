package com.starboost.starboost_backend_demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Gender is required")
    private String gender;       

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Role is required")
    private String role;         

    private String registrationNumber;

    private Long agencyId;       

    private Long regionId;       

    private String agencyName;

    private String regionName;

    private Boolean active;

    @NotBlank(message = "Password is required")
    private String password;
}
