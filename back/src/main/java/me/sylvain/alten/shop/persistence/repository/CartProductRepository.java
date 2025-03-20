package me.sylvain.alten.shop.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import me.sylvain.alten.shop.persistence.model.CartProduct;
import org.springframework.stereotype.Repository;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    List<CartProduct> findByCartId(Long cartId);
}
