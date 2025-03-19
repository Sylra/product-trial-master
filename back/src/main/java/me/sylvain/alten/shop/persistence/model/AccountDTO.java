package me.sylvain.alten.shop.persistence.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {
    @NotBlank(message = "Le nom d'utilisateur est requis")
    private String username;
    @NotBlank(message = "Le prénom est requis")
    private String firstname;
    @NotBlank(message = "L'email est requis")
    private String email;
    @NotBlank(message = "Le mot de passe est requis")
    private String password;
}
