package com.carvajalecomers.service;

import com.carvajalecomers.entity.Product;
import com.carvajalecomers.exception.ResourceNotFoundException;
import com.carvajalecomers.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Catalogo paginado de productos activos. Filtra por categoria (category)
     * y/o por termino de busqueda en el nombre (q) cuando se indican.
     */
    @Transactional(readOnly = true)
    public Page<Product> getCatalog(Pageable pageable, String category, String q) {
        boolean hasCategory = category != null && !category.isBlank();
        boolean hasQ = q != null && !q.isBlank();

        if (hasCategory && hasQ) {
            return productRepository.findByActiveTrueAndNameContainingIgnoreCaseAndCategoryIgnoreCase(
                    q.trim(), category.trim(), pageable);
        }
        if (hasCategory) {
            return productRepository.findByActiveTrueAndCategoryIgnoreCase(category.trim(), pageable);
        }
        if (hasQ) {
            return productRepository.findByActiveTrueAndNameContainingIgnoreCase(q.trim(), pageable);
        }
        return productRepository.findByActiveTrue(pageable);
    }

    @Transactional(readOnly = true)
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }
}