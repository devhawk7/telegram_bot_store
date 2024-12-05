package org.common.model;

import lombok.Data;

@Data
public class Basket extends BaseModel {
    private Long userId;
    private double totalPrice;
}
