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

import org.springframework.stereotype.Component;

@Component
public class UpdateBookCommandHandler implements CommandHandler<UpdateBookCommand, Void> {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;

    public UpdateBookCommandHandler(BookRepository bookRepository, AuthorRepository authorRepository,
            PublisherRepository publisherRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Void handle(UpdateBookCommand command) {

        Book book = bookRepository.findById(BookId.of(command.bookId()))
                .orElseThrow(() -> new IllegalArgumentException("Book not found" + command.bookId()));

        if (!categoryRepository.existsById(CategoryId.of(command.categoryId()))) {
            throw new IllegalArgumentException("Category not found" + command.categoryId());
        }

        if (!publisherRepository.existsById(PublisherId.of(command.publisherId()))) {
            throw new IllegalArgumentException("Publisher not found" + command.publisherId());
        }

        for (UUID authorId : command.authorIds()) {
            if (!authorRepository.existsById(AuthorId.of(authorId))) {
                throw new IllegalArgumentException("Author not found" + authorId);
            }
        }

        List<AuthorId> authorIds = command.authorIds().stream().map(AuthorId::of).toList();

        book.updateDetails(command.title(), CategoryId.of(command.categoryId()), PublisherId.of(command.publisherId()),
                command.publishedDate(), command.pageCount(), command.language(), command.description(), authorIds);

        bookRepository.save(book);
        return null;
    }

}
