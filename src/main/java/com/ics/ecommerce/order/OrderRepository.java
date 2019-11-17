package com.ics.ecommerce.order;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, String> {

    @Query("SELECT o FROM Order o WHERE o.placed >= :start and o.placed <= :end")
    List<Order> findOrdersWithinDateRange(LocalDateTime start, LocalDateTime end);
}
