package com.main.jackpotcontribution;

import com.main.jackpotcontribution.core.domain.Jackpot;
import com.main.jackpotcontribution.core.domain.JackpotContribution;
import com.main.jackpotcontribution.core.service.VariableRewardEvaluationTypeService;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VariableRewardEvaluationTypeServiceTests {

    @Test
    void evaluateContributingBetForReward_shouldReturnTrue_whenRandomLessThanChance() {
        Supplier<Double> fakeRandom = () -> 10.0; // deterministic random
        VariableRewardEvaluationTypeService service = new VariableRewardEvaluationTypeService(fakeRandom);

        Jackpot jackpot = mock(Jackpot.class);
        when(jackpot.getInitialRewardChancePercentage()).thenReturn(20.0);
        when(jackpot.getPoolLimit()).thenReturn(100.0);
        when(jackpot.getTotalContribution()).thenReturn(50.0);

        JackpotContribution contribution = mock(JackpotContribution.class);
        when(contribution.getJackpot()).thenReturn(jackpot);

        boolean result = service.evaluateContributingBetForReward(contribution);

        assertThat(result).isTrue(); // 10 < 20 + (80 * 0.5) = 60
    }

    @Test
    void evaluateContributingBetForReward_shouldReturnFalse_whenRandomGreaterThanChance() {
        Supplier<Double> fakeRandom = () -> 90.0; // deterministic random
        VariableRewardEvaluationTypeService service = new VariableRewardEvaluationTypeService(fakeRandom);

        Jackpot jackpot = mock(Jackpot.class);
        when(jackpot.getInitialRewardChancePercentage()).thenReturn(20.0);
        when(jackpot.getPoolLimit()).thenReturn(100.0);
        when(jackpot.getTotalContribution()).thenReturn(50.0);

        JackpotContribution contribution = mock(JackpotContribution.class);
        when(contribution.getJackpot()).thenReturn(jackpot);

        boolean result = service.evaluateContributingBetForReward(contribution);

        assertThat(result).isFalse(); // 90 > 60
    }
}
