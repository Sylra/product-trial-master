package me.sylvain.alten.shop.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import me.sylvain.alten.shop.persistence.model.Cart;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {}
