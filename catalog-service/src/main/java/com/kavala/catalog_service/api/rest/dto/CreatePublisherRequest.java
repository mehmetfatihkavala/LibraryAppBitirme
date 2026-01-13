package com.kavala.catalog_service.api.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePublisherRequest(
        @NotBlank String name) {

}
