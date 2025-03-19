package me.sylvain.alten.shop.persistence.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDTO {
    @NotBlank(message = "L'email est requis")
    private String email;
    @NotBlank(message = "Le mot de passe est requis")
    private String password;
}
