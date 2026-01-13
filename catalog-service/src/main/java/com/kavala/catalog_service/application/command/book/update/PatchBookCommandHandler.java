package com.kavala.catalog_service.application.command.book.update;

import java.util.List;
import java.util.UUID;

import com.kavala.catalog_service.core.cqrs.CommandHandler;
import com.kavala.catalog_service.domain.author.AuthorId;
import com.kavala.catalog_service.domain.book.model.Book;
import com.kavala.catalog_service.domain.book.model.BookId;
import com.kavala.catalog_service.domain.category.CategoryId;
import com.kavala.catalog_service.domain.port.AuthorRepository;
import com.kavala.catalog_service.domain.port.BookRepository;
import com.kavala.catalog_service.domain.port.CategoryRepository;
import com.kavala.catalog_service.domain.port.PublisherRepository;
import com.kavala.catalog_service.domain.publisher.PublisherId;

public class PatchBookCommandHandler implements CommandHandler<PatchBookCommand, Void> {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;

    public PatchBookCommandHandler(BookRepository bookRepository, CategoryRepository categoryRepository,
            PublisherRepository publisherRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public Void handle(PatchBookCommand command) {

        Book book = bookRepository.findById(BookId.of(command.bookId()))
                .orElseThrow(() -> new IllegalArgumentException("Book not found" + command.bookId()));

        if (command.categoryId() != null && !categoryRepository.existsById(CategoryId.of(command.categoryId()))) {
            throw new IllegalArgumentException("Category not found" + command.categoryId());
        }

        if (command.publisherId() != null && !publisherRepository.existsById(PublisherId.of(command.publisherId()))) {
            throw new IllegalArgumentException("Publisher not found" + command.publisherId());
        }

        if (command.authorIds() != null) {
            for (UUID authorId : command.authorIds()) {
                if (!authorRepository.existsById(AuthorId.of(authorId))) {
                    throw new IllegalArgumentException("Author not found" + authorId);
                }
            }
        }

        // “patch” için domain’de ayrı metod yazmak en temizidir. Sonradan bak...
        List<AuthorId> authorIds = (command.authorIds() == null)
                ? book.getAuthorIds()
                : command.authorIds().stream().map(AuthorId::of).toList();

        book.updateDetails(
                command.title() != null ? command.title() : book.getTitle(),
                command.categoryId() != null ? CategoryId.of(command.categoryId()) : book.getCategoryId(),
                command.publisherId() != null ? PublisherId.of(command.publisherId()) : book.getPublisherId(),
                command.publishedDate() != null ? command.publishedDate() : book.getPublishedDate(),
                command.pageCount() != null ? command.pageCount() : book.getPageCount(),
                command.language() != null ? command.language() : book.getLanguage(),
                command.description() != null ? command.description() : book.getDescription(),
                authorIds);

        bookRepository.save(book);
        return null;
    }

}
