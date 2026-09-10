package com.carvajalecomers.controller;

import com.carvajalecomers.dto.ProductPageResponse;
import com.carvajalecomers.dto.ProductResponse;
import com.carvajalecomers.entity.Product;
import com.carvajalecomers.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final int MAX_PAGE_SIZE = 100;

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Catalogo paginado. Params: page (default 0), size (default 12),
     * sort (ej: "name,asc" o "price,desc"), category y q (busqueda por nombre).
     */
    @GetMapping
    public ResponseEntity<ProductPageResponse> getCatalog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String q) {
        Pageable pageable = buildPageable(page, size, sort);
        Page<Product> products = productService.getCatalog(pageable, category, q);
        return ResponseEntity.ok(ProductPageResponse.fromPage(products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        return ResponseEntity.ok(ProductResponse.fromEntity(product));
    }

    private Pageable buildPageable(int page, int size, String sort) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);

        if (sort != null && !sort.isBlank()) {
            String[] parts = sort.split(",");
            String field = parts[0].trim();
            Sort.Direction direction = (parts.length > 1 && parts[1].trim().equalsIgnoreCase("desc"))
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            return PageRequest.of(safePage, safeSize, Sort.by(direction, field));
        }
        return PageRequest.of(safePage, safeSize, Sort.by("id").ascending());
    }
}