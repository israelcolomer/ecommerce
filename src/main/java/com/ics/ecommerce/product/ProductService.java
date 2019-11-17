package com.ics.ecommerce.product;

import com.ics.ecommerce.common.NotFoundException;

import java.util.Collection;
import java.util.List;

public interface ProductService {

    /**
     * Retrieves a product by it's SKU
     * @param sku the id to update
     * @return the retrieved product if found, otherwise throws {@link NotFoundException}
     * @throws NotFoundException
     */
    Product get(String sku) throws NotFoundException;

    /**
     * Persists the provided product
     * @param toCreate
     * @return
     * @throws NotFoundException
     */
    Product create(Product toCreate);

    /**
     * Retrieves a product by it's SKU
     * @param sku the id to update
     * @return the updated product if found, otherwise throws {@link NotFoundException}
     * @throws NotFoundException
     */
    void update(String sku, Product toUpdate) throws NotFoundException;

    /**
     * Retrieves all products, ordered by creation date desc
     * @return the all products
     */
    List<Product> fetchAllProducts();

    /**
     * Retrieves all products matching the provided list of ids
     * @return the all products
     */
    List<Product> fetchProductsBySkus(Collection<String> skus);

}
