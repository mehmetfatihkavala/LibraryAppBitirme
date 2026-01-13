package com.kavala.catalog_service.application.query.author;

import java.util.List;

import org.springframework.stereotype.Component;

import com.kavala.catalog_service.api.rest.dto.AuthorResponse;
import com.kavala.catalog_service.core.cqrs.QueryHandler;
import com.kavala.catalog_service.domain.author.Author;
import com.kavala.catalog_service.domain.port.AuthorRepository;

/**
 * Tüm yazarları listeleyen Query Handler.
 * 
 * ListAuthorQuery'yi işleyerek sayfalı AuthorResponse listesi döndürür.
 * Sayfalama repository seviyesinde uygulanabilir.
 */
@Component
public class ListAuthorQueryHandler implements QueryHandler<ListAuthorQuery, List<AuthorResponse>> {

    private final AuthorRepository authorRepository;

    public ListAuthorQueryHandler(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<AuthorResponse> handle(ListAuthorQuery query) {
        List<Author> authors = authorRepository.findAll();

        // Basit sayfalama (repository seviyesinde optimize edilebilir)
        int start = query.page() * query.size();
        int end = Math.min(start + query.size(), authors.size());

        if (start >= authors.size()) {
            return List.of();
        }

        return authors.subList(start, end).stream()
                .map(author -> new AuthorResponse(
                        author.getAuthorId().value(),
                        author.getFullName()))
                .toList();
    }
}
