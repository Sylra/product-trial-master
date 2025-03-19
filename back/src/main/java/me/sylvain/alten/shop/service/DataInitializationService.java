package me.sylvain.alten.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import me.sylvain.alten.shop.EnvUtil;
import me.sylvain.alten.shop.persistence.model.Account;
import me.sylvain.alten.shop.persistence.repository.AccountRepository;

@Service
public class DataInitializationService {

    @Autowired
    private AccountRepository accountRepository;

    @PostConstruct
    public void init() {
        if (!accountRepository.findByEmail(EnvUtil.ADMIN_EMAIL).isPresent()) {
            Account adminUser = new Account();
            adminUser.setUsername("admin");
            adminUser.setFirstname("admin");
            adminUser.setEmail(EnvUtil.ADMIN_EMAIL);
            adminUser.setPassword("password");
            accountRepository.save(adminUser);
        }
    }
}