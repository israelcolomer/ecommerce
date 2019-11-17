package com.ics.ecommerce.product;

import com.ics.ecommerce.common.NotFoundException;
import com.ics.ecommerce.date.DateFormat;
import com.ics.ecommerce.date.DateService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
// TODO: Only special users (admin?) should be able to create/update products. Implement these restrictions once authentication is added
public class ProductServiceImpl implements ProductService {

    private final DateService dateService;
    private final ProductRepository productRepository;

    public ProductServiceImpl(DateService dateService, ProductRepository productRepository) {
        this.dateService = dateService;
        this.productRepository = productRepository;
    }

    @Override
    public Product get(String sku) throws NotFoundException {
        return productRepository.findById(sku).orElseThrow(() -> new NotFoundException(Product.class, sku));
    }

    @Override
    public Product create(Product toCreate) {
        toCreate.setCreated((LocalDateTime) dateService.now(DateFormat.ISO_DATE_TIME));
        return productRepository.save(toCreate);
    }

    @Override
    public void update(String sku, Product toUpdate) {
        productRepository.update(toUpdate.getSku(), toUpdate.getName(), toUpdate.getDescription(), toUpdate.getPrice(), toUpdate.isDeleted());
    }

    public List<Product> fetchAllProducts() {
        return productRepository.findAll(new Sort(Sort.Direction.DESC, "created"));
    }

    @Override
    public List<Product> fetchProductsBySkus(Collection<String> skus) {
        return productRepository.getProductsBySkus(skus);
    }
}
