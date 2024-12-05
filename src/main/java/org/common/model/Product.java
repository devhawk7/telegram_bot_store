package org.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class Product extends BaseModel {
    private String name;
    private int quantity;
    private double price;
    private UUID categoryId;
    private String productPhoto;
}
