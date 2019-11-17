package com.ics.ecommerce.order;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ics.ecommerce.BaseControllerIT;
import com.ics.ecommerce.EcommerceApplicationIT;
import com.ics.ecommerce.date.DateFormat;
import com.ics.ecommerce.product.Product;
import com.ics.ecommerce.product.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = EcommerceApplicationIT.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-it.properties")
@Slf4j
public class OrderControllerIT extends BaseControllerIT {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderLineEntryRepository orderLineEntryRepository;

    @Before
    public void clearTable() {
        orderLineEntryRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    public void whenCreateOrder_thenOrderIsCreatedAsExpected() throws Exception {
        List<String> skus = createProducts(2).stream().map(Product::getSku).collect(Collectors.toList());
        List<CreateOrderRequest.SkuData> entries = buildSkuData(skus);
        CreateOrderRequest createOrderRequest = buildOrderRequest(entries);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(OrderController.BASE_URI)
                .content(objectMapper.writeValueAsString(createOrderRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Order created = objectMapper.readValue(result.getResponse().getContentAsString(), Order.class);

        assertThat(created.getId(), notNullValue());

        assertThat(created.getEntries().size(), CoreMatchers.is(2));

        assertThat(created.getEntries().get(0).getSku(), is(skus.get(0)));
        assertThat(created.getEntries().get(0).getQuantity(), is(1));
        assertThat(created.getEntries().get(0).getPrice(), notNullValue());
        assertThat(created.getEntries().get(0).getOrder(), is(created));

        assertThat(created.getEntries().get(1).getSku(), is(skus.get(1)));
        assertThat(created.getEntries().get(1).getQuantity(), is(2));
        assertThat(created.getEntries().get(1).getPrice(), notNullValue());
        assertThat(created.getEntries().get(1).getOrder(), is(created));
    }

    @Test
    public void whenGetOrdersByDateRange_thenExpectedOrderAreRetrieved() throws Exception {
        LocalDate start = LocalDate.of(2019, 11, 15);
        LocalDate end = LocalDate.of(2019, 11, 20);
        createOrders(start, end);

        // Wide range, all orders
        String startParam = dateService.format(LocalDate.of(2019, 11, 14).atStartOfDay(), DateFormat.ISO_DATE_TIME);
        String endParam = dateService.format(LocalDate.of(2019, 11, 21).atTime(LocalTime.MAX), DateFormat.ISO_DATE_TIME);

        List<Order> orders = fetchOrders(startParam, endParam);

        assertThat(orders.size(), is(5));
        assertThat(orders.get(0).getPlaced().toLocalDate(), is(LocalDate.of(2019, 11, 15)));
        assertThat(orders.get(1).getPlaced().toLocalDate(), is(LocalDate.of(2019, 11, 16)));
        assertThat(orders.get(2).getPlaced().toLocalDate(), is(LocalDate.of(2019, 11, 17)));
        assertThat(orders.get(3).getPlaced().toLocalDate(), is(LocalDate.of(2019, 11, 18)));
        assertThat(orders.get(4).getPlaced().toLocalDate(), is(LocalDate.of(2019, 11, 19)));

        // Narrow range, filtered orders
        startParam = dateService.format(LocalDate.of(2019, 11, 17).atStartOfDay(), DateFormat.ISO_DATE_TIME);
        endParam = dateService.format(LocalDate.of(2019, 11, 19).atTime(LocalTime.MAX), DateFormat.ISO_DATE_TIME);

        orders = fetchOrders(startParam, endParam);

        assertThat(orders.size(), is(3));
        assertThat(orders.get(0).getPlaced().toLocalDate(), is(LocalDate.of(2019, 11, 17)));
        assertThat(orders.get(1).getPlaced().toLocalDate(), is(LocalDate.of(2019, 11, 18)));
        assertThat(orders.get(2).getPlaced().toLocalDate(), is(LocalDate.of(2019, 11, 19)));
    }

    private List<Order> fetchOrders(String startParam, String endParam) throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(OrderController.BASE_URI + "/search")
                .param("start", startParam)
                .param("end", endParam))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Order> orders = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Order>>(){});
        log.debug("Orders: {}", orders);

        return orders;
    }

    private void createOrders(LocalDate from, LocalDate to) {
        IntStream.range(0, (int) ChronoUnit.DAYS.between(from, to)).forEach(i -> {
            // Update the current date, to simulate the system clock ticking
            LocalDate today = from.plusDays(i);
            dateService.setCurrentTime(LocalDateTime.of(today, LocalTime.now()));

            try {
                List<String> skus = createProducts(1).stream().map(Product::getSku).collect(Collectors.toList());
                List<CreateOrderRequest.SkuData> entries = buildSkuData(skus);
                CreateOrderRequest createOrderRequest = buildOrderRequest(entries);

                createOrder(createOrderRequest);
            } catch (Exception e) {
                fail();
            }
        });
    }

    private MvcResult createOrder(CreateOrderRequest createOrderRequest) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.post(OrderController.BASE_URI)
                .content(objectMapper.writeValueAsString(createOrderRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
    }

}
