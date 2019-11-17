package com.ics.ecommerce.order;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class CreateOrderRequest {
    private List<SkuData> skuData;

    @Data
    @Accessors(chain = true)
    public static class SkuData {
        private String sku;
        private int quantity;
    }
}
