package com.kavala.catalog_service.core.cqrs;

public interface CommandHandler<C extends Command<R>, R> {
    R handle(C command);
}
