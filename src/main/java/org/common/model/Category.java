package org.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class Category extends BaseModel{
    private String name;
    private UUID parentId;
}
