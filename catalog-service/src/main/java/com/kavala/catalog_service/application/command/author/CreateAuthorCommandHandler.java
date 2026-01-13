package com.kavala.catalog_service.application.command.author;

import java.util.UUID;

import com.kavala.catalog_service.core.cqrs.CommandHandler;
import com.kavala.catalog_service.domain.author.Author;
import com.kavala.catalog_service.domain.author.AuthorId;
import com.kavala.catalog_service.domain.port.AuthorRepository;

import org.springframework.stereotype.Component;

@Component
public class CreateAuthorCommandHandler implements CommandHandler<CreateAuthorCommand, UUID> {

    private final AuthorRepository authorRepository;

    public CreateAuthorCommandHandler(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public UUID handle(CreateAuthorCommand command) {
        AuthorId authorId = AuthorId.generate();
        Author author = Author.createAuthor(authorId, command.name(), command.birthDate(), command.nationality());
        authorRepository.save(author);
        return authorId.value();
    }
}
