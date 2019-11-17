package com.ics.ecommerce.order;

import java.util.List;

public interface OrderLineEntryService {
    /**
     * Persists the provided order line entries
     *
     * @param orderLineEntries
     * @return the persisted order line entries
     */
    List<OrderLineEntry> saveAll(List<OrderLineEntry> orderLineEntries);
}
