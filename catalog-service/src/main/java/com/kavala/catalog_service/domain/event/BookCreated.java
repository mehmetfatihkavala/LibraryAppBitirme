package com.kavala.catalog_service.domain.event;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import com.kavala.catalog_service.domain.author.AuthorId;
import com.kavala.catalog_service.domain.book.model.BookId;
import com.kavala.catalog_service.domain.category.CategoryId;
import com.kavala.catalog_service.domain.publisher.PublisherId;

public record BookCreated(BookId bookId, String title, CategoryId categoryId,
                PublisherId publisherId, LocalDate publishedDate,
                Integer pageCount, String language, String description,
                List<AuthorId> authorIds, Instant createdAt) {

}
