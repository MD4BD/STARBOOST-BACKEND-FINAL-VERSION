package com.starboost.starboost_backend_demo.dto.admin;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String gender;
    private String dateOfBirth;
    private String role;
    private Long agencyId;
    private Long regionId;
    private Boolean active;
}
