package me.sylvain.alten.shop.persistence.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import me.sylvain.alten.shop.validation.ValidEnum;

@Getter
@Setter
public class ProductDTO {
    @NotBlank(message = "Le code est requis")
    private String code;
    @NotBlank(message = "Le nom est requis")
    private String name;
    private String description;
    private String image;
    private String category;
    @NotNull(message = "Le prix est requis")
    @Min(value = 0, message = "Le prix doit être positif")
    private Double price;
    @NotNull(message = "La quantité est requise")
    @Min(value = 0, message = "La quantité doit être positive")
    private Integer quantity;
    private String internalReference;
    private Long shellId;
    @ValidEnum(enumClass = InventoryStatus.class, message = "Valeur incorrecte : seuls les choix suivants sont autorisés : INSTOCK, LOWSTOCK ou OUTOFSTOCK")
    private String inventoryStatus;
    @Min(value = 0, message = "Le rating doit être positif")
    private Integer rating;
}
