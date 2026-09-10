package com.carvajalecomers.dto;

import com.carvajalecomers.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Respuesta paginada del catalogo. Contiene la pagina actual de productos
 * mas los metadatos de paginacion (page, size, totalElements, totalPages,
 * first, last).
 */
public class ProductPageResponse {

    private List<ProductResponse> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;

    public ProductPageResponse() {
    }

    public ProductPageResponse(List<ProductResponse> content, int page, int size,
                               long totalElements, int totalPages,
                               boolean first, boolean last) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.first = first;
        this.last = last;
    }

    public static ProductPageResponse fromPage(Page<Product> page) {
        List<ProductResponse> content = page.getContent().stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
        return new ProductPageResponse(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }

    public List<ProductResponse> getContent() {
        return content;
    }

    public void setContent(List<ProductResponse> content) {
        this.content = content;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}