package com.ics.ecommerce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ics.ecommerce.date.TimeTweakerDateServiceImpl;
import com.ics.ecommerce.order.CreateOrderRequest;
import com.ics.ecommerce.product.Product;
import com.ics.ecommerce.product.ProductController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BaseControllerIT {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected TimeTweakerDateServiceImpl dateService;

    private final Random random = new Random();

    protected List<CreateOrderRequest.SkuData> buildSkuData(List<String> skus) {
        return IntStream.range(0, skus.size())
                .mapToObj(i -> new CreateOrderRequest.SkuData().setSku(String.valueOf(i + 1)).setQuantity(i + 1))
                .collect(Collectors.toList());
    }

    protected List<Product> createProducts(int numProducts) {
        return saveProducts(numProducts);
    }

    protected List<Product> saveProducts(int numProducts) {
        LocalDateTime now = LocalDateTime.now();

        return IntStream.range(0, numProducts)
                .mapToObj(i -> {
                    try {
                        Product product = new Product()
                                .setSku(String.valueOf(i + 1))
                                .setName("Name " + i)
                                .setDescription("Desc " + i)
                                .setPrice(random.nextDouble() * 100)
                                .setCreated(now.plusDays(i));

                        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(ProductController.BASE_URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(product)))
                                .andExpect(MockMvcResultMatchers.status().isCreated())
                                .andReturn();

                        return objectMapper.readValue(result.getResponse().getContentAsString(), Product.class);
                    }catch (Exception e) {
                        throw new RuntimeException("Failed to create products");
                    }
                })
                .collect(Collectors.toList());
    }

    protected CreateOrderRequest buildOrderRequest(List<CreateOrderRequest.SkuData> skuData) {
        return new CreateOrderRequest().setSkuData(skuData);
    }

}
