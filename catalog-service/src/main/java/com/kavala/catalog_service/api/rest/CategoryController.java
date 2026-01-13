package com.kavala.catalog_service.api.rest;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kavala.catalog_service.api.rest.dto.CategoryResponse;
import com.kavala.catalog_service.api.rest.dto.CreateCategoryRequest;
import com.kavala.catalog_service.application.command.category.CreateCategoryCommand;
import com.kavala.catalog_service.application.command.category.CreateCategoryCommandHandler;
import com.kavala.catalog_service.application.query.category.ListCategoriesQuery;
import com.kavala.catalog_service.application.query.category.ListCategoriesQueryHandler;

import jakarta.validation.Valid;

/**
 * Kategori (Category) işlemlerini yöneten REST Controller.
 * 
 * Hem Command (Create) hem de Query (List) işlemlerini içerir.
 * Küçük entity'ler için ayrı controller yerine tek controller tercih
 * edilmiştir.
 * CQRS pattern'i handler seviyesinde uygulanmaktadır.
 */
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CreateCategoryCommandHandler createCategoryCommandHandler;
    private final ListCategoriesQueryHandler listCategoriesQueryHandler;

    public CategoryController(
            CreateCategoryCommandHandler createCategoryCommandHandler,
            ListCategoriesQueryHandler listCategoriesQueryHandler) {
        this.createCategoryCommandHandler = createCategoryCommandHandler;
        this.listCategoriesQueryHandler = listCategoriesQueryHandler;
    }

    /**
     * Yeni bir kategori oluşturur.
     * 
     * İstek gövdesinde name alanı beklenir.
     * Kategori adı boş olamaz ve maksimum 255 karakter olmalıdır.
     * 
     * @param request Kategori oluşturma isteği (CreateCategoryRequest)
     * @return 201 Created - Oluşturulan kategorinin UUID'si ve URI'si
     */
    @PostMapping
    public ResponseEntity<UUID> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        CreateCategoryCommand command = new CreateCategoryCommand(request.name());

        UUID categoryId = createCategoryCommandHandler.handle(command);

        return ResponseEntity
                .created(URI.create("/api/v1/categories/" + categoryId))
                .body(categoryId);
    }

    /**
     * Tüm kategorileri sayfalı olarak listeler.
     * 
     * Kategoriler kitaplara atanmak üzere listelenir.
     * Sayfalama ile büyük veri setlerinde performans sağlanır.
     * 
     * @param page Sayfa numarası (0'dan başlar, varsayılan: 0)
     * @param size Sayfa başına kayıt sayısı (1-100 arası, varsayılan: 20)
     * @return 200 OK - Kategori listesi (List<CategoryResponse>)
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> listCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        ListCategoriesQuery query = new ListCategoriesQuery(page, size);

        List<CategoryResponse> response = listCategoriesQueryHandler.handle(query);

        return ResponseEntity.ok(response);
    }
}
