package com.main.jackpotcontribution.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record JackpotBetRequest(
        @NotBlank String betId,
        @NotBlank String userId,
        @NotBlank String jackpotId,
        @Positive @NotNull Double betAmount
) {
}
