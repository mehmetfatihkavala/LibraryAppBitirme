
package com.kavala.catalog_service.application.query.book.get;

import java.util.List;

import com.kavala.catalog_service.api.rest.dto.AuthorResponse;
import com.kavala.catalog_service.api.rest.dto.BookDetailResponse;
import com.kavala.catalog_service.api.rest.dto.CategoryResponse;
import com.kavala.catalog_service.api.rest.dto.PublisherResponse;
import com.kavala.catalog_service.core.cqrs.QueryHandler;
import com.kavala.catalog_service.domain.book.model.Book;
import com.kavala.catalog_service.domain.book.model.BookId;
import com.kavala.catalog_service.domain.port.BookRepository;
import com.kavala.catalog_service.domain.port.CategoryRepository;
import com.kavala.catalog_service.domain.port.PublisherRepository;
import com.kavala.catalog_service.domain.port.AuthorRepository;
import com.kavala.catalog_service.domain.category.Category;
import com.kavala.catalog_service.domain.publisher.Publisher;
import com.kavala.catalog_service.domain.author.Author;

public class GetBookQueryHandler implements QueryHandler<GetBookQuery, BookDetailResponse> {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;

    public GetBookQueryHandler(BookRepository bookRepository, CategoryRepository categoryRepository,
            PublisherRepository publisherRepository, AuthorRepository authorRepository) {
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public BookDetailResponse handle(GetBookQuery query) {

        Book book = bookRepository.findById(BookId.of(query.bookId()))
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + query.bookId()));

        Category category = categoryRepository.findById(book.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        CategoryResponse categoryResponse = new CategoryResponse(
                category.getCategoryId().value(),
                category.getName());

        Publisher publisher = publisherRepository.findById(book.getPublisherId())
                .orElseThrow(() -> new IllegalArgumentException("Publisher not found"));

        PublisherResponse publisherResponse = new PublisherResponse(
                publisher.getPublisherId().value(),
                publisher.getName());

        List<AuthorResponse> authorResponses = book.getAuthorIds().stream()
                .map(authorId -> {
                    Author author = authorRepository.findById(authorId)
                            .orElseThrow(() -> new IllegalArgumentException("Author not found"));
                    return new AuthorResponse(
                            author.getAuthorId().value(),
                            author.getFullName());
                })
                .toList();
        return new BookDetailResponse(
                book.getId().value(),
                book.getTitle(),
                categoryResponse,
                publisherResponse,
                book.getPublishedDate(),
                book.getPageCount(),
                book.getLanguage(),
                book.getDescription(),
                authorResponses);
    }

}
