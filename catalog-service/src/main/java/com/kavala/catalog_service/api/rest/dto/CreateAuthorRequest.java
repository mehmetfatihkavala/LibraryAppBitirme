package com.kavala.catalog_service.api.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAuthorRequest(
        @NotBlank String name) {

}
