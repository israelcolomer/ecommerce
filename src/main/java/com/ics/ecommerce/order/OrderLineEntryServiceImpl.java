package com.ics.ecommerce.order;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderLineEntryServiceImpl implements OrderLineEntryService {

    private final OrderLineEntryRepository orderLineEntryRepository;

    public OrderLineEntryServiceImpl(OrderLineEntryRepository orderLineEntryRepository) {
        this.orderLineEntryRepository = orderLineEntryRepository;
    }

    @Override
    public List<OrderLineEntry> saveAll(List<OrderLineEntry> orderLineEntry) {
        return orderLineEntryRepository.saveAll(orderLineEntry);
    }
}
