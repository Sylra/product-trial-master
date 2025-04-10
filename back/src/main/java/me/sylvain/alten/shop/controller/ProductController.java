package me.sylvain.alten.shop.controller;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import me.sylvain.alten.shop.EnvUtil;
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
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(product.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO dto) {
        if (!isAdminUser()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

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
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(product));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        if (!isAdminUser()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

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
            return ResponseEntity.ok(productRepository.save(product));
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (!isAdminUser()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        if (productRepository.findById(id).isPresent()) {
            productRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    private boolean isAdminUser() {
        UserDetails account = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return EnvUtil.ADMIN_EMAIL.equals(account.getUsername());
    }
}
