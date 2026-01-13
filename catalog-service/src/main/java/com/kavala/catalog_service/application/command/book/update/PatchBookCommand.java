package com.kavala.catalog_service.application.command.book.update;

import java.util.UUID;

import com.kavala.catalog_service.core.cqrs.Command;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PatchBookCommand(
        UUID bookId,
        @Size(max = 255) String title,
        @Positive Integer pageCount,
        LocalDate publishedDate,
        UUID categoryId,
        UUID publisherId,
        @Size(max = 50) String language,
        @Size(max = 2000) String description,
        List<UUID> authorIds) implements Command<Void> {
}