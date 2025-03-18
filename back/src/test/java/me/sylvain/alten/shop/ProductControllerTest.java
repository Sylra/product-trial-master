package me.sylvain.alten.shop;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import me.sylvain.alten.shop.controller.ProductController;
import me.sylvain.alten.shop.persistence.model.InventoryStatus;
import me.sylvain.alten.shop.persistence.model.Product;
import me.sylvain.alten.shop.persistence.repository.ProductRepository;

@WebMvcTest(ProductController.class)
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb")
class ProductControllerTest {

    final Product product = Product.builder()
        .id(1L)
        .code("ABCD")
        .name("Alten phone")
        .price(100.0)
        .quantity(10)
        .inventoryStatus(InventoryStatus.INSTOCK.name())
        .createdAt(1742247846L)
        .build();
    final Product updatedProduct = Product.builder()
        .id(1L)
        .code("ABCD")
        .name("Alten phone")
        .price(100.0)
        .quantity(10)
        .inventoryStatus(InventoryStatus.INSTOCK.name())
        .createdAt(1742247846L)
        .updatedAt(1742248846L)
        .build();

    final String PRODUCT_JSON = "{\"id\":1,\"code\":\"ABCD\",\"name\":\"Alten phone\",\"description\":null,\"image\":null,\"category\":null,\"price\":100.0,\"quantity\":10,\"internalReference\":null,\"shellId\":null,\"inventoryStatus\":\"INSTOCK\",\"rating\":null,\"createdAt\":1742247846,\"updatedAt\":null}";
    final String UPDATED_PRODUCT_JSON = "{\"id\":1,\"code\":\"ABCD\",\"name\":\"Alten phone\",\"description\":null,\"image\":null,\"category\":null,\"price\":100.0,\"quantity\":10,\"internalReference\":null,\"shellId\":null,\"inventoryStatus\":\"INSTOCK\",\"rating\":null,\"createdAt\":1742247846,\"updatedAt\":1742248846}";
    final String PRODUCT_JSON_ARRAY = "["+PRODUCT_JSON+"]";

    final String CREATE_PRODUCT_JSON = "{\"name\":\"Alten phone\",\"code\":\"ABCD\",\"quantity\":\"10\",\"price\":100.0,\"inventoryStatus\":\"INSTOCK\"}";
    final String UPDATE_PRODUCT_JSON = CREATE_PRODUCT_JSON;
    final String INVALID_PRODUCT_JSON = "{\"code\":\"ABCD\",\"quantity\":\"10\",\"price\":100.0,\"inventoryStatus\":\"INSTOCK\"}";

    final String NAME_REQUIRED_ERROR = "{\"name\":\"Le nom est requis\"}";

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ProductRepository repository;

    // Vérifie que GET /products retourne une liste JSON vide
	@Test
	void testGetAllProductsReturnsEmptyJsonArray() throws Exception {
		when(repository.findAll()).thenReturn(new ArrayList<>());
		this.mockMvc.perform(get("/products"))
            .andExpect(status().isOk())
			.andExpect(content().string(containsString("[]")));
	}

    // Vérifie que GET /products retourne une liste JSON avec un produit
	@Test
	void testGetAllProductsReturnsJsonArray() throws Exception {
		when(repository.findAll()).thenReturn(Collections.singletonList(product));
		this.mockMvc.perform(get("/products"))
            .andExpect(status().isOk())
			.andExpect(content().string(containsString(PRODUCT_JSON_ARRAY)));
	}

    // Vérifie que GET /products/1 retourne un objet JSON avec un produit
	@Test
	void testGetOneProductsReturnsProductJsonObject() throws Exception {
		when(repository.findById(1L)).thenReturn(Optional.of(product));
		this.mockMvc.perform(get("/products/1"))
            .andExpect(status().isOk())
			.andExpect(content().string(containsString(PRODUCT_JSON)));
	}

    // Vérifie que GET /products/404 retourne une erreur 404
	@Test
	void testNonExistentProductReturnsNotFound() throws Exception {
		this.mockMvc.perform(get("/products/404"))
            .andExpect(status().isNotFound());
	}    

    // Vérifie que POST /products avec un produit valide retourne un objet JSON avec le produit créé
	@Test
	void testCreateProductWithMandatoryFieldsReturnsOk() throws Exception {
		when(repository.save(any())).thenReturn(product);
		this.mockMvc.perform(post("/products")
            .header("Content-Type", "application/json").content(CREATE_PRODUCT_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(PRODUCT_JSON)));
	}

    // Vérifie que POST /products avec un produit sans nom (obligatoire) retourne une erreur 400 et un message d'erreur
	@Test
	void testCreateProductWithMissingMandatoryFieldsIsInvalid() throws Exception {
		this.mockMvc.perform(post("/products")
            .header("Content-Type", "application/json").content(INVALID_PRODUCT_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString(NAME_REQUIRED_ERROR)));
	}

    // Vérifie que PATCH /products/1 avec un produit valide retourne un objet JSON avec le produit mis à jour
	@Test
	void testUpdateProductWithMandatoryFieldsReturnsOk() throws Exception {
		when(repository.findById(1L)).thenReturn(Optional.of(product));
		when(repository.save(any())).thenReturn(updatedProduct);
		this.mockMvc.perform(patch("/products/1")
            .header("Content-Type", "application/json").content(UPDATE_PRODUCT_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(UPDATED_PRODUCT_JSON)));
	}

    // Vérifie que PATCH /products/1 avec un produit sans nom (obligatoire) retourne une erreur 400 et un message d'erreur
	@Test
	void testUpdateProductWithMissingMandatoryFieldsIsInvalid() throws Exception {
		this.mockMvc.perform(patch("/products/1")
            .header("Content-Type", "application/json").content(INVALID_PRODUCT_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString(NAME_REQUIRED_ERROR)));
	}

    // Vérifie que PUT /products/404 retourne une erreur 404
	@Test
	void testUpdateProductWithNonExistentProductReturnsNotFound() throws Exception {
		this.mockMvc.perform(patch("/products/404")
            .header("Content-Type", "application/json").content(UPDATE_PRODUCT_JSON))
            .andExpect(status().isNotFound());
	}    

    // Vérifie que DELETE /products/1 retourne Ok
	@Test
	void testDeleteProductReturnsOk() throws Exception {
		when(repository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(repository).deleteById(1L);
		this.mockMvc.perform(delete("/products/1"))
            .andExpect(status().isNoContent());
	}

    // Vérifie que DELETE /products/404 retourne une erreur 404
	@Test
	void testDeleteProductWithNonExistentProductReturnsNotFound() throws Exception {
		this.mockMvc.perform(delete("/products/404"))
            .andExpect(status().isNotFound());
	} 
}