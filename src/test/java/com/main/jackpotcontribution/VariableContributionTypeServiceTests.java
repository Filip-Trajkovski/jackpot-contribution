package com.main.jackpotcontribution;

import com.main.jackpotcontribution.core.domain.Jackpot;
import com.main.jackpotcontribution.core.service.VariableContributionTypeService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VariableContributionTypeServiceTests {

    private final VariableContributionTypeService service = new VariableContributionTypeService();


    @Test
    void evaluateContributionByBet_shouldReturnCorrectContribution_whenPoolIsEmpty() {
        Jackpot jackpot = mock(Jackpot.class);
        when(jackpot.getId()).thenReturn("jackpot-uuid");
        when(jackpot.getTotalContribution()).thenReturn(0.0);
        when(jackpot.getPoolLimit()).thenReturn(1000.0);
        when(jackpot.getInitialContributionPercentage()).thenReturn(10.0);

        Double result = service.evaluateContributionByBet(jackpot, 200.0);

        // pool is empty, percentage = initial = 10%, so 10% of 200 = 20
        assertThat(result).isCloseTo(20.0, within(1e-9));
    }

    @Test
    void evaluateContributionByBet_shouldReduceContribution_whenPoolPartiallyFilled() {
        Jackpot jackpot = mock(Jackpot.class);
        when(jackpot.getId()).thenReturn("jackpot-uuid");
        when(jackpot.getTotalContribution()).thenReturn(250.0); // 25% of 1000
        when(jackpot.getPoolLimit()).thenReturn(1000.0);
        when(jackpot.getInitialContributionPercentage()).thenReturn(20.0);

        Double result = service.evaluateContributionByBet(jackpot, 100.0);

        // pool filled = 25% => 5 steps => percentage = 20 * (1 - 0.25) = 15%
        // contribution = 15% of 100 = 15
        assertThat(result).isCloseTo(15.0, within(1e-9));
    }

    @Test
    void evaluateContributionByBet_shouldReturnZero_whenPoolAlmostFull() {
        Jackpot jackpot = mock(Jackpot.class);
        when(jackpot.getId()).thenReturn("jackpot-uuid");
        when(jackpot.getTotalContribution()).thenReturn(950.0); // 95% of 1000
        when(jackpot.getPoolLimit()).thenReturn(1000.0);
        when(jackpot.getInitialContributionPercentage()).thenReturn(10.0);

        Double result = service.evaluateContributionByBet(jackpot, 100.0);

        // pool filled = 95% => 19 steps => percentage = 10 * (1 - 0.95) = 0.5%
        // contribution = 0.5% of 100 = 0.5
        assertThat(result).isCloseTo(0.5, within(1e-9));
    }

    @Test
    void evaluateContributionByBet_shouldReturnMinimalContribution_whenPoolFull() {
        Jackpot jackpot = mock(Jackpot.class);
        when(jackpot.getId()).thenReturn("jackpot-uuid");
        when(jackpot.getTotalContribution()).thenReturn(1000.0); // 100% of pool
        when(jackpot.getPoolLimit()).thenReturn(1000.0);
        when(jackpot.getInitialContributionPercentage()).thenReturn(10.0);

        Double result = service.evaluateContributionByBet(jackpot, 100.0);

        // pool filled = 100% => 20 steps (floor(100/5)) => percentage = 10 * (1 - 1.0) = 0
        // contribution = 0% of 100 = 0
        assertThat(result).isCloseTo(0.0, within(1e-9));
    }
}
