package com.ics.ecommerce.order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {


    /**
     * Persists the provided order.
     * TODO: the (authenticated) user making the request should be used
     *
     * @param order
     * @return The persisted order
     */
    Order create(Order order);

    /**
     * Finds orders between the provided date range
     *
     * @param start start of range, inclusive
     * @param end end of range, inclusive
     * @return Orders within the date range
     */
    List<Order> findOrdersWithinDateRange(LocalDateTime start, LocalDateTime end);
}
