package me.sylvain.alten.shop;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import me.sylvain.alten.shop.controller.ProductController;

@SpringBootTest
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb")
class AppTest {

	@Autowired
	private ProductController productController;

	@Test
	void contextLoads() {
		assertThat(productController).isNotNull();
	}
}
