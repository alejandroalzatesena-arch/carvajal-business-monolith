package com.carvajal.wishlist.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carvajal.wishlist.dto.product.ProductResponse;
import com.carvajal.wishlist.entity.Product;
import com.carvajal.wishlist.exception.ResourceNotFoundException;
import com.carvajal.wishlist.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll(String category, String name) {
        boolean hasCategory = category != null && !category.isBlank();
        boolean hasName = name != null && !name.isBlank();

        List<Product> products;
        if (hasCategory && hasName) {
            products = productRepository.findByCategoryAndNameContainingIgnoreCaseAndActiveTrueOrderByNameAsc(category.trim(), name.trim());
        } else if (hasCategory) {
            products = productRepository.findByCategoryAndActiveTrueOrderByNameAsc(category.trim());
        } else if (hasName) {
            products = productRepository.findByNameContainingIgnoreCaseAndActiveTrueOrderByNameAsc(name.trim());
        } else {
            products = productRepository.findByActiveTrueOrderByNameAsc();
        }
        return products.stream().map(ProductResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        Product product = productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        return ProductResponse.from(product);
    }
}
