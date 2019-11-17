package com.ics.ecommerce.order;

import com.ics.ecommerce.date.DateFormat;
import com.ics.ecommerce.date.DateService;
import com.ics.ecommerce.product.Product;
import com.ics.ecommerce.product.ProductService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ics.ecommerce.order.OrderController.BASE_URI;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping(BASE_URI)
@CrossOrigin
public class OrderController {

    public static final String BASE_URI = "/api/order";

    private final OrderService orderService;

    private final ProductService productService;

    private final OrderLineEntryService orderLineEntryService;

    private final DateService dateService;

    public OrderController(
            OrderService orderService,
            ProductService productService,
            OrderLineEntryService orderLineEntryService,
            DateService dateService) {
        this.orderService = orderService;
        this.productService = productService;
        this.orderLineEntryService = orderLineEntryService;
        this.dateService = dateService;
    }

    @PostMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Order create(@RequestBody CreateOrderRequest createOrderRequest) {
        Order order = new Order();
        order.setPlaced((LocalDateTime) dateService.now(DateFormat.ISO_DATE_TIME));

        Map<String, CreateOrderRequest.SkuData> skuDataMap =
                createOrderRequest.getSkuData().stream()
                        .collect(Collectors.toMap(CreateOrderRequest.SkuData::getSku, x -> x));

        List<Product> products = productService.fetchProductsBySkus(skuDataMap.keySet());

        List<OrderLineEntry> entries = products.stream().map(product -> {
            CreateOrderRequest.SkuData skuData = skuDataMap.get(product.getSku());

            OrderLineEntry orderLineEntry = new OrderLineEntry();
            orderLineEntry.setSku(skuData.getSku());
            orderLineEntry.setPrice(product.getPrice());
            orderLineEntry.setQuantity(skuData.getQuantity());
            orderLineEntry.setOrder(order);

            return orderLineEntry;
        }).collect(Collectors.toList());

        order.setEntries(entries);

        Order saved = orderService.create(order);

        orderLineEntryService.saveAll(entries);

        return saved;
    }

    @GetMapping(path = "/search", produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Order> search(
            @RequestParam(value = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(value = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return orderService.findOrdersWithinDateRange(start, end);
    }
}
