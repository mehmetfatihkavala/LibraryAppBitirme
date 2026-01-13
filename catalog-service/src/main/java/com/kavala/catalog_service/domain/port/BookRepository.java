package com.kavala.catalog_service.domain.port;

import java.util.List;
import java.util.Optional;

import com.kavala.catalog_service.domain.book.model.Book;
import com.kavala.catalog_service.domain.book.model.BookId;

public interface BookRepository {
    Book save(Book book);

    Book update(Book book);

    Optional<Book> findById(BookId bookId);

    List<Book> findAll();

    void delete(Book book);

    boolean existsById(BookId bookId);

}
