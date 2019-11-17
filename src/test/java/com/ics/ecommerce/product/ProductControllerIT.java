package com.ics.ecommerce.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ics.ecommerce.BaseControllerIT;
import com.ics.ecommerce.EcommerceApplicationIT;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = EcommerceApplicationIT.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-it.properties")
@Slf4j
public class ProductControllerIT extends BaseControllerIT {

    @Autowired
    private ProductRepository productRepository;

    @Before
    public void clearTable() {
        productRepository.deleteAll();
    }

    @Test
    public void whenCreateProduct_thenProductIsCreatedAsExpected() throws Exception {
        List<String> skus = createProducts(3).stream().map(Product::getSku).collect(Collectors.toList());

        Product product = retrieveProduct(skus.get(0));

        assertThat(product, notNullValue());
        assertThat(product.getSku(), is(skus.get(0)));
        assertThat(product.getName(), notNullValue());
        assertThat(product.getDescription(), notNullValue());
        assertThat(product.getPrice(), notNullValue());
        assertThat(product.getCreated(), notNullValue());
    }


    @Test
    public void whenListProducts_thenAllProductsAreRetrieved() throws Exception {
        List<String> skus = createProducts(3).stream().map(Product::getSku).collect(Collectors.toList());

        List<Product> products = fetchProducts();

        assertThat(products.isEmpty(), is(false));

        assertThat(products.size(), CoreMatchers.is(3));

        assertThat(products.get(0).getSku(), is(skus.get(0)));
        assertThat(products.get(1).getSku(), is(skus.get(1)));
        assertThat(products.get(2).getSku(), is(skus.get(2)));
    }

    @Test
    public void whenProductUpdate_thenProductIsUpdatedAsExpected() throws Exception {
        Product original = createProducts(1).get(0);

        Product toUpdate = new Product();
        BeanUtils.copyProperties(original, toUpdate);

        toUpdate.setPrice(1000D);
        toUpdate.setCreated(LocalDateTime.now());

        updateProduct(toUpdate);

        Product updated = retrieveProduct(toUpdate.getSku());

        assertThat(updated.getCreated(), is(original.getCreated()));
        assertThat(updated.getCreated(), is(not(toUpdate.getCreated())));

        assertThat(updated.getPrice(), is(toUpdate.getPrice()));
        assertThat(updated.getPrice(), is(not(original.getPrice())));
    }

    private void updateProduct(Product toUpdate) throws Exception {
        mvc.perform(MockMvcRequestBuilders.put(ProductController.BASE_URI + "/" + toUpdate.getSku())
                .content(objectMapper.writeValueAsBytes(toUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    private Product retrieveProduct(String sku) throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(ProductController.BASE_URI + "/" + sku))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        return objectMapper.readValue(result.getResponse().getContentAsString(), Product.class);
    }

    private List<Product> fetchProducts() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(ProductController.BASE_URI + "/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Product> products = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Product>>(){});
        log.debug("Product: {}", products);

        return products;
    }
}
