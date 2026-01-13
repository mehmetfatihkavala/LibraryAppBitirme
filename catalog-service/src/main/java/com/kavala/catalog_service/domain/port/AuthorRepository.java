package com.kavala.catalog_service.domain.port;

import java.util.List;
import java.util.Optional;

import com.kavala.catalog_service.domain.author.Author;
import com.kavala.catalog_service.domain.author.AuthorId;

public interface AuthorRepository {
    Author save(Author author);

    Author update(Author author);

    Optional<Author> findById(AuthorId authorId);

    List<Author> findAll();

    void delete(Author author);

    boolean existsById(AuthorId authorId);
}
