package com.kavala.catalog_service.infrastructure.adapter.persistence.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.kavala.catalog_service.domain.book.model.Book;
import com.kavala.catalog_service.infrastructure.adapter.persistence.entity.JpaBookEntity;
import com.kavala.catalog_service.infrastructure.adapter.persistence.mapper.BookMapper;
import com.kavala.catalog_service.infrastructure.adapter.persistence.repo.SpringDataBookJpaRepository;

/**
 * Kitap arama işlemlerini yöneten adapter.
 * Veritabanı üzerinde filtreleme ve arama yapar.
 * İleride Elasticsearch gibi bir arama motoru ile değiştirilebilir.
 */
@Component
public class BookSearchAdapter {

    private final SpringDataBookJpaRepository jpaRepository;
    private final BookMapper mapper;

    public BookSearchAdapter(SpringDataBookJpaRepository jpaRepository, BookMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * Anahtar kelimeye göre kitap arar.
     * Başlık ve açıklama alanlarında case-insensitive arama yapar.
     * 
     * @param keyword Aranacak anahtar kelime
     * @return Eşleşen kitapların listesi
     */
    public List<Book> searchByKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return jpaRepository.findAll().stream()
                    .map(mapper::toDomain)
                    .collect(Collectors.toList());
        }

        return jpaRepository.searchByKeyword(keyword).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * Dile göre kitapları filtreler.
     * 
     * @param language Dil kodu
     * @return O dildeki kitapların listesi
     */
    public List<Book> findByLanguage(String language) {
        return jpaRepository.findByLanguage(language).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * Sayfa sayısı aralığına göre kitapları filtreler.
     * 
     * @param minPageCount Minimum sayfa sayısı
     * @param maxPageCount Maksimum sayfa sayısı
     * @return Belirtilen aralıktaki kitapların listesi
     */
    public List<Book> findByPageCountRange(Integer minPageCount, Integer maxPageCount) {
        int min = minPageCount != null ? minPageCount : 0;
        int max = maxPageCount != null ? maxPageCount : Integer.MAX_VALUE;

        return jpaRepository.findByPageCountBetween(min, max).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * Çoklu filtreleme ile kitap arar.
     * Tüm filtreler AND mantığı ile uygulanır.
     * 
     * @param keyword      Anahtar kelime (opsiyonel)
     * @param language     Dil (opsiyonel)
     * @param minPageCount Minimum sayfa sayısı (opsiyonel)
     * @param maxPageCount Maksimum sayfa sayısı (opsiyonel)
     * @return Filtrelere uyan kitapların listesi
     */
    public List<Book> search(String keyword, String language, Integer minPageCount, Integer maxPageCount) {
        List<JpaBookEntity> results = jpaRepository.findAll();

        // Keyword filtresi
        if (keyword != null && !keyword.isBlank()) {
            String lowerKeyword = keyword.toLowerCase();
            results = results.stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(lowerKeyword) ||
                            (book.getDescription() != null &&
                                    book.getDescription().toLowerCase().contains(lowerKeyword)))
                    .collect(Collectors.toList());
        }

        // Language filtresi
        if (language != null && !language.isBlank()) {
            results = results.stream()
                    .filter(book -> book.getLanguage().equalsIgnoreCase(language))
                    .collect(Collectors.toList());
        }

        // Min page count filtresi
        if (minPageCount != null) {
            results = results.stream()
                    .filter(book -> book.getPageCount() >= minPageCount)
                    .collect(Collectors.toList());
        }

        // Max page count filtresi
        if (maxPageCount != null) {
            results = results.stream()
                    .filter(book -> book.getPageCount() <= maxPageCount)
                    .collect(Collectors.toList());
        }

        return results.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
