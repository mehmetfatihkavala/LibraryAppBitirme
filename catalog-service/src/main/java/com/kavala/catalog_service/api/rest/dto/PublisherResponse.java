package com.kavala.catalog_service.api.rest.dto;

import java.util.UUID;

public record PublisherResponse(
        UUID id,
        String name) {

}
