package me.sylvain.alten.shop.controller;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.sylvain.alten.shop.persistence.model.Product;
import me.sylvain.alten.shop.persistence.model.ProductCreateDTO;
import me.sylvain.alten.shop.persistence.model.ProductUpdateDTO;
import me.sylvain.alten.shop.persistence.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);

        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Product> createUser(@RequestBody ProductCreateDTO dto) {
        Product product = new Product();
        if (dto.getCode() != null) product.setCode(dto.getCode());
        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getDescription() != null) product.setDescription(dto.getDescription());
        if (dto.getImage() != null) product.setImage(dto.getImage());
        if (dto.getCategory() != null) product.setCategory(dto.getCategory());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getQuantity() != null) product.setQuantity(dto.getQuantity());
        if (dto.getInternalReference() != null) product.setInternalReference(dto.getInternalReference());
        if (dto.getShellId() != null) product.setShellId(dto.getShellId());
        if (dto.getInventoryStatus() != null) product.setInventoryStatus(dto.getInventoryStatus());
        if (dto.getRating() != null) product.setRating(dto.getRating());
        product.setCreatedAt(Instant.now().getEpochSecond());
        return ResponseEntity.ok(productService.saveProduct(product));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateDTO dto) {
        return productService.getProductById(id).map(product -> {
            if (dto.getCode() != null) product.setCode(dto.getCode());
            if (dto.getName() != null) product.setName(dto.getName());
            if (dto.getDescription() != null) product.setDescription(dto.getDescription());
            if (dto.getImage() != null) product.setImage(dto.getImage());
            if (dto.getCategory() != null) product.setCategory(dto.getCategory());
            if (dto.getPrice() != null) product.setPrice(dto.getPrice());
            if (dto.getQuantity() != null) product.setQuantity(dto.getQuantity());
            if (dto.getInternalReference() != null) product.setInternalReference(dto.getInternalReference());
            if (dto.getShellId() != null) product.setShellId(dto.getShellId());
            if (dto.getInventoryStatus() != null) product.setInventoryStatus(dto.getInventoryStatus());
            if (dto.getRating() != null) product.setRating(dto.getRating());
            product.setUpdatedAt(Instant.now().getEpochSecond());
            productService.saveProduct(product);
            return ResponseEntity.ok(product);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.getProductById(id).isPresent()) {
            productService.removeProduct(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}