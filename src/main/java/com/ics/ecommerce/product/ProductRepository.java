package com.ics.ecommerce.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("select p from Product p where p.sku in (:skuList)")
    List<Product> getProductsBySkus(Collection<String> skuList);

    @Modifying
    @Query("update Product p set p.name = :name, p.description = :description, p.price = :price, p.deleted = :deleted where p.sku = :sku")
    void update(String sku, String name, String description, double price, boolean deleted);
}
