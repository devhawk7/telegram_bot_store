package org.common.model;

import lombok.Data;

import java.util.UUID;

@Data
public abstract class BaseModel {
    private UUID id;

    public BaseModel() {
        this.id = UUID.randomUUID();
    }
}
