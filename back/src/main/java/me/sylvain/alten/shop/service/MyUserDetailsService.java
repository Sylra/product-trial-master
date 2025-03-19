package me.sylvain.alten.shop.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import me.sylvain.alten.shop.persistence.model.Account;
import me.sylvain.alten.shop.persistence.repository.AccountRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Account not found"));
        return new User(account.getEmail(), "{noop}" + account.getPassword(), new ArrayList<>());
    }
}