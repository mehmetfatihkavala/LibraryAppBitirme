package com.kavala.catalog_service.application.query.book.search;

import java.util.List;

import com.kavala.catalog_service.api.rest.dto.BookSearchResponse;
import com.kavala.catalog_service.core.cqrs.Query;

public record SearchBooksQuery(
                String keyword,
                String language,
                Integer minPageCount,
                Integer maxPageCount) implements Query<List<BookSearchResponse>> {

        // Sadece keyword ile arama için constructor
        public SearchBooksQuery(String keyword) {
                this(keyword, null, null, null);
        }

        // Keyword ve language ile arama için constructor
        public SearchBooksQuery(String keyword, String language) {
                this(keyword, language, null, null);
        }
}
