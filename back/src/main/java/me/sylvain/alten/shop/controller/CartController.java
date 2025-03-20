package me.sylvain.alten.shop.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.sylvain.alten.shop.persistence.model.Cart;
import me.sylvain.alten.shop.persistence.model.CartProduct;
import me.sylvain.alten.shop.persistence.model.Product;
import me.sylvain.alten.shop.persistence.repository.ProductRepository;
import me.sylvain.alten.shop.persistence.repository.CartRepository;
import me.sylvain.alten.shop.persistence.repository.CartProductRepository;

@RestController
@RequestMapping("/carts")
@Validated
public class CartController {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;

    public CartController(ProductRepository productRepository,
                          CartRepository cartRepository,
                          CartProductRepository cartProductRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartProductRepository = cartProductRepository;
    }

    @PostMapping
    public ResponseEntity<Cart> createCart() {
        Cart savedCart = cartRepository.save(new Cart());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCart);
    }

    @PostMapping("/{cartId}/products/{productId}")
    public ResponseEntity<Void> addProductToCart(@PathVariable Long cartId, @PathVariable Long productId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) return ResponseEntity.notFound().build();

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return ResponseEntity.notFound().build();

        CartProduct cartProduct = new CartProduct();
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);

        cartProductRepository.save(cartProduct);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{cartId}/products")
    public ResponseEntity<List<CartProduct>> getCartProducts(@PathVariable Long cartId) {
        List<CartProduct> cartProducts = cartProductRepository.findByCartId(cartId);
        return ResponseEntity.ok(cartProducts);
    }

    @DeleteMapping("/{cartId}/products/{cartProductId}")
    public ResponseEntity<Void> removeProductFromCart(@PathVariable Long cartId, @PathVariable Long cartProductId) {
        CartProduct cartProduct = cartProductRepository.findById(cartProductId).orElse(null);
        if (cartProduct == null) return ResponseEntity.notFound().build(); 

        cartProductRepository.delete(cartProduct);
        return ResponseEntity.ok().build();
    }
}
