package me.sylvain.alten.shop.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import me.sylvain.alten.shop.persistence.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a WHERE a.email = ?1")
    Optional<Account> findByEmail(String email);
}
