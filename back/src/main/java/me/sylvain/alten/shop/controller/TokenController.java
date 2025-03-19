package me.sylvain.alten.shop.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import me.sylvain.alten.shop.persistence.model.TokenDTO;
import me.sylvain.alten.shop.persistence.repository.AccountRepository;
import me.sylvain.alten.shop.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/token")
@Validated
public class TokenController {
    AccountRepository accountRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public TokenController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostMapping
    public String login(@Valid @RequestBody TokenDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        return jwt;
    }
}
