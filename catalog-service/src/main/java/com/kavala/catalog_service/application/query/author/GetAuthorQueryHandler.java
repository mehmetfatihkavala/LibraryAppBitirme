package com.kavala.catalog_service.application.query.author;

import org.springframework.stereotype.Component;

import com.kavala.catalog_service.api.rest.dto.AuthorResponse;
import com.kavala.catalog_service.core.cqrs.QueryHandler;
import com.kavala.catalog_service.domain.author.Author;
import com.kavala.catalog_service.domain.author.AuthorId;
import com.kavala.catalog_service.domain.port.AuthorRepository;

/**
 * Tek bir yazarı ID'ye göre getiren Query Handler.
 * 
 * GetAuthorQuery'yi işleyerek AuthorResponse döndürür.
 */
@Component
public class GetAuthorQueryHandler implements QueryHandler<GetAuthorQuery, AuthorResponse> {

    private final AuthorRepository authorRepository;

    public GetAuthorQueryHandler(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorResponse handle(GetAuthorQuery query) {
        Author author = authorRepository.findById(AuthorId.of(query.authorId()))
                .orElseThrow(() -> new IllegalArgumentException("Author not found: " + query.authorId()));

        return new AuthorResponse(
                author.getAuthorId().value(),
                author.getFullName());
    }
}
