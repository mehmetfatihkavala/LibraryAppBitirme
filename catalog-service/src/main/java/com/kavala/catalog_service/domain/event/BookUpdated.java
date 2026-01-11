package com.kavala.catalog_service.domain.event;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import com.kavala.catalog_service.domain.author.AuthorId;
import com.kavala.catalog_service.domain.book.model.BookId;
import com.kavala.catalog_service.domain.category.CategoryId;
import com.kavala.catalog_service.domain.publisher.PublisherId;

public record BookUpdated(BookId bookId, String newTitle, CategoryId newCategoryId,
                PublisherId newPublisherId, LocalDate newPublishedDate,
                Integer newPageCount, String newLanguage, String newDescription,
                List<AuthorId> newAuthorIds, Instant updatedAt) {

}
