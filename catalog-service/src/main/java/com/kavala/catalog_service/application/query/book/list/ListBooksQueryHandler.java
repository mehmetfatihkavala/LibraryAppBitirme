package com.kavala.catalog_service.application.query.book.list;

import java.util.List;

import org.springframework.stereotype.Component;

import com.kavala.catalog_service.api.rest.dto.AuthorResponse;
import com.kavala.catalog_service.api.rest.dto.BookListResponse;
import com.kavala.catalog_service.api.rest.dto.CategoryResponse;
import com.kavala.catalog_service.api.rest.dto.PublisherResponse;
import com.kavala.catalog_service.core.cqrs.QueryHandler;
import com.kavala.catalog_service.domain.author.Author;
import com.kavala.catalog_service.domain.book.model.Book;
import com.kavala.catalog_service.domain.category.Category;
import com.kavala.catalog_service.domain.publisher.Publisher;
import com.kavala.catalog_service.domain.port.BookRepository;
import com.kavala.catalog_service.domain.port.CategoryRepository;
import com.kavala.catalog_service.domain.port.PublisherRepository;
import com.kavala.catalog_service.domain.port.AuthorRepository;

@Component
public class ListBooksQueryHandler implements QueryHandler<ListBooksQuery, List<BookListResponse>> {

        private final BookRepository bookRepository;
        private final CategoryRepository categoryRepository;
        private final PublisherRepository publisherRepository;
        private final AuthorRepository authorRepository;

        public ListBooksQueryHandler(
                        BookRepository bookRepository,
                        CategoryRepository categoryRepository,
                        PublisherRepository publisherRepository,
                        AuthorRepository authorRepository) {
                this.bookRepository = bookRepository;
                this.categoryRepository = categoryRepository;
                this.publisherRepository = publisherRepository;
                this.authorRepository = authorRepository;
        }

        @Override
        public List<BookListResponse> handle(ListBooksQuery query) {
                List<Book> books = bookRepository.findAll();

                return books.stream()
                                .map(this::toBookListResponse)
                                .toList();
        }

        private BookListResponse toBookListResponse(Book book) {
                // Category bilgisi
                Category category = categoryRepository.findById(book.getCategoryId())
                                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

                CategoryResponse categoryResponse = new CategoryResponse(
                                category.getCategoryId().value(),
                                category.getName());

                // Publisher bilgisi
                Publisher publisher = publisherRepository.findById(book.getPublisherId())
                                .orElseThrow(() -> new IllegalArgumentException("Publisher not found"));

                PublisherResponse publisherResponse = new PublisherResponse(
                                publisher.getPublisherId().value(),
                                publisher.getName());

                // Author bilgileri
                List<AuthorResponse> authorResponses = book.getAuthorIds().stream()
                                .map(authorId -> {
                                        Author author = authorRepository.findById(authorId)
                                                        .orElseThrow(() -> new IllegalArgumentException(
                                                                        "Author not found"));
                                        return new AuthorResponse(
                                                        author.getAuthorId().value(),
                                                        author.getFullName());
                                })
                                .toList();

                return new BookListResponse(
                                book.getId().value(),
                                book.getTitle(),
                                categoryResponse,
                                publisherResponse,
                                book.getPublishedDate(),
                                book.getPageCount(),
                                book.getLanguage(),
                                authorResponses);
        }

}
