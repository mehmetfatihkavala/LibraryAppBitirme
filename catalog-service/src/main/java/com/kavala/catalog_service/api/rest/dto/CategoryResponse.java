package com.kavala.catalog_service.api.rest.dto;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name) {

}
