package org.bot.model;

import lombok.Builder;
import lombok.Data;
import org.bot.model.enums.State;

@Data
@Builder
public class User {
    private long chatId;
    private State state;
}
