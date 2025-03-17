package me.sylvain.alten.shop.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import me.sylvain.alten.shop.persistence.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {}
