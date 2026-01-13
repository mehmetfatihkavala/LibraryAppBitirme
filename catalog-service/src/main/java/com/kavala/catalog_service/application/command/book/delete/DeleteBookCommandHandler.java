package com.kavala.catalog_service.application.command.book.delete;

import com.kavala.catalog_service.core.cqrs.CommandHandler;
import com.kavala.catalog_service.domain.book.model.Book;
import com.kavala.catalog_service.domain.book.model.BookId;
import com.kavala.catalog_service.domain.port.BookRepository;

public class DeleteBookCommandHandler implements CommandHandler<DeleteBookCommand, Void> {

    private final BookRepository bookRepository;

    public DeleteBookCommandHandler(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Void handle(DeleteBookCommand command) {

        Book book = bookRepository.findById(BookId.of(command.bookId()))
                .orElseThrow(() -> new IllegalArgumentException("Book not found" + command.bookId()));

        bookRepository.delete(book);
        return null;
    }

}
