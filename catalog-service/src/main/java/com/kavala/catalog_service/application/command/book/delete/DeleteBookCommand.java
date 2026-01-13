package com.kavala.catalog_service.application.command.book.delete;

import java.util.UUID;

import com.kavala.catalog_service.core.cqrs.Command;

import jakarta.validation.constraints.NotNull;

public record DeleteBookCommand(
        @NotNull UUID bookId) implements Command<Void> {

}
