package com.kavala.catalog_service.api.rest.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAuthorRequest(
                @NotBlank String name,
                @NotNull LocalDate birthDate,
                @NotBlank String nationality) {

}
