package com.main.jackpotcontribution.core.domain.dto;

public record JackpotBetDTO(
        String betId,
        String userId,
        String jackpotId,
        Double betAmount
) {
}
