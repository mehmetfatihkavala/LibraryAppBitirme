package com.kavala.catalog_service.application.command.book.create;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.kavala.catalog_service.core.cqrs.Command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateBookCommand(
                @NotBlank @Size(max = 255) String title,
                @NotNull UUID categoryId,
                @NotNull UUID publisherId,
                LocalDate publishedDate,
                @NotNull @Positive Integer pageCount,
                @NotBlank String language,
                String description,
                @NotEmpty List<UUID> authorIds) implements Command<UUID> {
}
