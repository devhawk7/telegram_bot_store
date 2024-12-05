package org.common.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface BaseService<T> {
    List<T> list() throws IOException;
    List<T> getById(UUID id) throws IOException;
}
