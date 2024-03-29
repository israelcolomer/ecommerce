package com.ics.ecommerce.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineEntryRepository extends JpaRepository<OrderLineEntry, String> {
}
