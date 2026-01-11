package com.kavala.catalog_service.domain.event;

import com.kavala.catalog_service.domain.book.model.BookId;

public record BookDeleted(BookId bookId) {

}
