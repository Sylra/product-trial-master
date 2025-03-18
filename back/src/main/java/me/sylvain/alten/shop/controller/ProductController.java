package me.sylvain.alten.shop.controller;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import me.sylvain.alten.shop.persistence.model.Product;
import me.sylvain.alten.shop.persistence.model.ProductDTO;
import me.sylvain.alten.shop.persistence.repository.ProductRepository;

@RestController
@RequestMapping("/products")
@Validated
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO dto) {
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
        return ResponseEntity.ok(productRepository.save(product));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        return productRepository.findById(id).map(product -> {
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
            productRepository.save(product);
            return ResponseEntity.ok(product);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productRepository.findById(id).isPresent()) {
            productRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}