package me.sylvain.alten.shop.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private String name;
    private String description;
    private String image;
    private String category;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private Integer quantity;
    private String internalReference;
    private Long shellId;
    @Column(nullable = false)
    private String inventoryStatus;
    private Integer rating;
    private Long createdAt;
    private Long updatedAt;
}
