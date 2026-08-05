package com.carvajal.wishlist.config;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.carvajal.wishlist.entity.Product;
import com.carvajal.wishlist.entity.User;
import com.carvajal.wishlist.entity.enums.Role;
import com.carvajal.wishlist.repository.ProductRepository;
import com.carvajal.wishlist.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public DataInitializer(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        seedProducts();
        seedUsers();
    }

    private void seedProducts() {
        if (productRepository.count() > 0) {
            return;
        }
        List<Product> products = List.of(
                new Product("PROD-001", "Televisor Smart 50\" 4K", "Televisor LED con resolución 4K UHD y sistema Smart TV.",
                        new BigDecimal("2199000"), 12, "Tecnología", null),
                new Product("PROD-002", "Licuadora Oster 600W", "Licuadora de alta potencia con jarra de vidrio.",
                        new BigDecimal("299900"), 0, "Electrodomésticos", null),
                new Product("PROD-003", "Horno Microondas 0.7L", "Horno microondas digital con función descongelar.",
                        new BigDecimal("449900"), 8, "Electrodomésticos", null),
                new Product("PROD-004", "Plancha de Pelo Cerámica", "Plancha de pelo con placas de cerámica y temperatura ajustable.",
                        new BigDecimal("189900"), 0, "Belleza", null),
                new Product("PROD-005", "Cafetera Programable 12 tazas", "Cafetera con programador digital y plato térmico.",
                        new BigDecimal("219900"), 25, "Cocina", null));
        productRepository.saveAll(products);
        log.info("Productos de demostración creados: {}", products.size());
    }

    private void seedUsers() {
        if (userRepository.count() > 0) {
            return;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userRepository.saveAll(List.of(
                new User("juan.perez", "juan.perez@carvajal.com", encoder.encode("123456"), "Juan Pérez", Role.USER, true),
                new User("admin", "admin@carvajal.com", encoder.encode("admin123"), "Administrador", Role.ADMIN, true)));
        log.info("Usuarios de demostración creados. Usuario: juan.perez / 123456 - Admin: admin / admin123");
    }
}
