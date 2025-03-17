package me.sylvain.alten.shop.persistence.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductUpdateDTO {
    private String code;
    private String name;
    private String description;
    private String image;
    private String category;
    private Double price;
    private Integer quantity;
    private String internalReference;
    private Long shellId;
    private InventoryStatus inventoryStatus;
    private Integer rating;
    private Long updatedAt;
}
