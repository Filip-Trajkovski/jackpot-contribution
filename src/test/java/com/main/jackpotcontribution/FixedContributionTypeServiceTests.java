package com.main.jackpotcontribution;

import com.main.jackpotcontribution.core.domain.Jackpot;
import com.main.jackpotcontribution.core.service.FixedContributionTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FixedContributionTypeServiceTests {

    private FixedContributionTypeService service;

    @BeforeEach
    void setUp() {
        service = new FixedContributionTypeService();
    }

    @Test
    void evaluateContributionByBet_shouldReturnCorrectFixedContribution() {
        Jackpot jackpot = mock(Jackpot.class);
        when(jackpot.getInitialContributionPercentage()).thenReturn(10.0);

        Double result = service.evaluateContributionByBet(jackpot, 200.0);

        assertThat(result).isCloseTo(20.0, within(1e-9)); // 10% of 200
    }

    @Test
    void evaluateContributionByBet_shouldReturnZero_whenBetAmountIsZero() {
        Jackpot jackpot = mock(Jackpot.class);
        when(jackpot.getInitialContributionPercentage()).thenReturn(15.0);

        Double result = service.evaluateContributionByBet(jackpot, 0.0);

        assertThat(result).isEqualTo(0.0);
    }

}
