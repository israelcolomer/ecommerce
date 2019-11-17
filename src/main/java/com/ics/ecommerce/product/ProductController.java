package com.ics.ecommerce.product;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ics.ecommerce.product.ProductController.BASE_URI;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping(BASE_URI)
@CrossOrigin
public class ProductController {

    public static final String BASE_URI = "/api/product";

    private final ProductServiceImpl productServiceImpl;

    public ProductController(ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
    }

    @PostMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody Product product) {
        return productServiceImpl.create(product);
    }

    @PutMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public void update(@PathVariable String id, @RequestBody Product product) {
        productServiceImpl.update(id, product);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public Product get(@PathVariable String id) {
        return productServiceImpl.get(id);
    }

    @GetMapping(path = "/all", produces = APPLICATION_JSON_UTF8_VALUE)
    public List<Product> fetch() {
        return productServiceImpl.fetchAllProducts();
    }

}
