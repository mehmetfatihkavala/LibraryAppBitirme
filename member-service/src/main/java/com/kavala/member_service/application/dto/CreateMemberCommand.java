package com.kavala.member_service.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateMemberCommand(
        @NotBlank(message = "First name cannot be blank") String firstName,
        @NotBlank(message = "Last name cannot be blank") String lastName,
        @Email(message = "Email is not valid") String email,
        @NotBlank(message = "Phone cannot be blank") String phone) {
}
