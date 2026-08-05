package com.carvajal.wishlist.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carvajal.wishlist.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByCode(String code);

    List<Product> findByActiveTrueOrderByNameAsc();

    Optional<Product> findByIdAndActiveTrue(Long id);

    List<Product> findByCategoryAndActiveTrueOrderByNameAsc(String category);

    List<Product> findByNameContainingIgnoreCaseAndActiveTrueOrderByNameAsc(String name);

    List<Product> findByCategoryAndNameContainingIgnoreCaseAndActiveTrueOrderByNameAsc(String category, String name);
}
