package org.bot.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum State {
    MAIN("main"),
    CATEGORY("category"),
    CHILD_CATEGORY("child_category"),
    PRODUCT("product"),
    BASKET("basket");


    private final String step;
}
