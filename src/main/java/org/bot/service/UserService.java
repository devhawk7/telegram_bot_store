package org.bot.service;

import lombok.RequiredArgsConstructor;
import org.bot.model.User;
import org.bot.model.enums.State;

import java.io.IOException;

@RequiredArgsConstructor
public class UserService {
    public void setState(long chatId, State state) throws IOException {
        User user = User.builder()
                .chatId(chatId)
                .state(state)
                .build();
    }
}
