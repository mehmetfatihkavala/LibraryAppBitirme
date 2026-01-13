package com.kavala.catalog_service.application.command.author;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

import com.kavala.catalog_service.core.cqrs.Command;

public record CreateAuthorCommand(
                @NotBlank @Size(max = 255) String name,
                @NotNull LocalDate birthDate,
                @NotBlank @Size(max = 255) String nationality) implements Command<UUID> {

}
