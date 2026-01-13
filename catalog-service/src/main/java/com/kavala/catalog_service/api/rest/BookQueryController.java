package com.kavala.catalog_service.api.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kavala.catalog_service.api.rest.dto.BookDetailResponse;
import com.kavala.catalog_service.api.rest.dto.BookListResponse;
import com.kavala.catalog_service.api.rest.dto.BookSearchResponse;
import com.kavala.catalog_service.application.query.book.get.GetBookQuery;
import com.kavala.catalog_service.application.query.book.get.GetBookQueryHandler;
import com.kavala.catalog_service.application.query.book.list.ListBooksQuery;
import com.kavala.catalog_service.application.query.book.list.ListBooksQueryHandler;
import com.kavala.catalog_service.application.query.book.search.SearchBooksQuery;
import com.kavala.catalog_service.application.query.book.search.SearchBooksQueryHandler;

/**
 * Kitap okuma işlemlerini yöneten REST Controller.
 * CQRS pattern'ine uygun olarak sadece Query (Get, List, Search) işlemlerini
 * içerir.
 * Command işlemleri BookCommandController'da yer almaktadır.
 * 
 * @see BookCommandController - Yazma işlemleri için
 */
@RestController
@RequestMapping("/api/v1/books")
public class BookQueryController {

    private final GetBookQueryHandler getBookQueryHandler;
    private final ListBooksQueryHandler listBooksQueryHandler;
    private final SearchBooksQueryHandler searchBooksQueryHandler;

    public BookQueryController(
            GetBookQueryHandler getBookQueryHandler,
            ListBooksQueryHandler listBooksQueryHandler,
            SearchBooksQueryHandler searchBooksQueryHandler) {
        this.getBookQueryHandler = getBookQueryHandler;
        this.listBooksQueryHandler = listBooksQueryHandler;
        this.searchBooksQueryHandler = searchBooksQueryHandler;
    }

    /**
     * Belirtilen ID'ye sahip kitabın detaylarını getirir.
     * 
     * Dönen response, kitabın tüm detaylarını (title, category, publisher,
     * publishedDate, pageCount, language, description, authors) içerir.
     * 
     * @param id Kitap UUID'si
     * @return 200 OK - Kitap detayları (BookDetailResponse)
     * @throws IllegalArgumentException Kitap bulunamazsa
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookDetailResponse> getBook(@PathVariable UUID id) {
        GetBookQuery query = new GetBookQuery(id);

        BookDetailResponse response = getBookQueryHandler.handle(query);

        return ResponseEntity.ok(response);
    }

    /**
     * Tüm kitapları sayfalı olarak listeler.
     * 
     * Sayfalama parametreleri ile büyük veri setlerinde performans sağlanır.
     * Her bir kitap için temel bilgiler (title, category, publisher,
     * publishedDate, pageCount, language, authors) döner.
     * 
     * @param page Sayfa numarası (0'dan başlar, varsayılan: 0)
     * @param size Sayfa başına kayıt sayısı (1-100 arası, varsayılan: 20)
     * @return 200 OK - Kitap listesi (List<BookListResponse>)
     */
    @GetMapping
    public ResponseEntity<List<BookListResponse>> listBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        ListBooksQuery query = new ListBooksQuery(page, size);

        List<BookListResponse> response = listBooksQueryHandler.handle(query);

        return ResponseEntity.ok(response);
    }

    /**
     * Kitapları çeşitli kriterlere göre arar.
     * 
     * Arama parametreleri:
     * - keyword: Başlık veya açıklama içinde arama yapar (case-insensitive)
     * - language: Belirli bir dildeki kitapları filtreler
     * - minPageCount: Minimum sayfa sayısı filtresi
     * - maxPageCount: Maksimum sayfa sayısı filtresi
     * 
     * Tüm parametreler opsiyoneldir ve kombinasyon halinde AND mantığıyla çalışır.
     * 
     * @param keyword      Aranacak anahtar kelime (opsiyonel)
     * @param language     Dil filtresi (opsiyonel)
     * @param minPageCount Minimum sayfa sayısı (opsiyonel)
     * @param maxPageCount Maksimum sayfa sayısı (opsiyonel)
     * @return 200 OK - Arama sonuçları (List<BookSearchResponse>)
     */
    @GetMapping("/search")
    public ResponseEntity<List<BookSearchResponse>> searchBooks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer minPageCount,
            @RequestParam(required = false) Integer maxPageCount) {

        SearchBooksQuery query = new SearchBooksQuery(
                keyword,
                language,
                minPageCount,
                maxPageCount);

        List<BookSearchResponse> response = searchBooksQueryHandler.handle(query);

        return ResponseEntity.ok(response);
    }
}
