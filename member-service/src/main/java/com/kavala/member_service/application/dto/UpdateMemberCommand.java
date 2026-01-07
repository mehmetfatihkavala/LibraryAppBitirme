package com.kavala.member_service.application.dto;

import com.kavala.member_service.domain.model.MemberId;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateMemberCommand(MemberId memberId, @NotBlank(message = "First name cannot be blank") String firstName,
        @NotBlank(message = "Last name cannot be blank") String lastName,
        @Email(message = "Email is not valid") String email,
        @NotBlank(message = "Phone cannot be blank") String phone) {

}
