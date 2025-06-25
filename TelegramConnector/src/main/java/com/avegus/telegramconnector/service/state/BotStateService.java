package com.avegus.telegramconnector.service.state;

import com.avegus.telegramconnector.model.enums.BotState;

import java.util.Optional;

/**
 * Manages User state
 */
public interface BotStateService {
    void updateState(Long userId, String username, BotState newState);
    Optional<BotState> getCurrentState(Long userId);
}
