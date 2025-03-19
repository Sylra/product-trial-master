package me.sylvain.alten.shop.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import me.sylvain.alten.shop.persistence.model.Account;
import me.sylvain.alten.shop.persistence.model.AccountDTO;
import me.sylvain.alten.shop.persistence.repository.AccountRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/account")
@Validated
public class AccountController {
    AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostMapping
    public ResponseEntity<Void> createAccount(@Valid @RequestBody AccountDTO dto) {
        if (accountRepository.findByEmail(dto.getEmail()).isPresent()) return ResponseEntity.badRequest().build();

        Account account = new Account();
        if (dto.getEmail() != null) account.setEmail(dto.getEmail());
        if (dto.getFirstname() != null) account.setFirstname(dto.getFirstname());
        if (dto.getPassword() != null) account.setPassword(dto.getPassword());
        if (dto.getUsername() != null) account.setUsername(dto.getUsername());
        accountRepository.save(account);
        return ResponseEntity.ok().build();
    }
}
