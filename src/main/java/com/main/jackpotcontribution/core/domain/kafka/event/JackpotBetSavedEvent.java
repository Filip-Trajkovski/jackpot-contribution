package com.main.jackpotcontribution.core.domain.kafka.event;

public record JackpotBetSavedEvent(String betId, String userId, String jackpotId, Double betAmount) {
}
