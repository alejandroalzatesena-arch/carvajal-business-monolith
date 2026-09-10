package com.carvajalecomers.repository;

import com.carvajalecomers.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByActiveTrue(Pageable pageable);

    Page<Product> findByActiveTrueAndCategoryIgnoreCase(String category, Pageable pageable);

    Page<Product> findByActiveTrueAndNameContainingIgnoreCase(String q, Pageable pageable);

    Page<Product> findByActiveTrueAndNameContainingIgnoreCaseAndCategoryIgnoreCase(
            String q, String category, Pageable pageable);
}