package com.kavala.member_service.application.dto;

public record CreateMemberCommand(String firstName, String lastName, String email, String phone) {

}
