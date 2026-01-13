package com.kavala.catalog_service.application.command.category;

import java.util.UUID;

import com.kavala.catalog_service.core.cqrs.Command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryCommand(
        @NotBlank @Size(max = 255) String name) implements Command<UUID> {

}
