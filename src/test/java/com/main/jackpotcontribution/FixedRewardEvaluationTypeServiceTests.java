package com.main.jackpotcontribution;

import com.main.jackpotcontribution.core.domain.Jackpot;
import com.main.jackpotcontribution.core.domain.JackpotContribution;
import com.main.jackpotcontribution.core.service.FixedRewardEvaluationTypeService;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FixedRewardEvaluationTypeServiceTests {

    @Test
    void evaluateContributingBetForReward_shouldReturnTrue_whenRandomLessThanChance() {
        Supplier<Double> fakeRandom = () -> 10.0; // deterministic random
        FixedRewardEvaluationTypeService service = new FixedRewardEvaluationTypeService(fakeRandom);

        Jackpot jackpot = mock(Jackpot.class);
        when(jackpot.getInitialRewardChancePercentage()).thenReturn(20.0);

        JackpotContribution contribution = mock(JackpotContribution.class);
        when(contribution.getJackpot()).thenReturn(jackpot);

        boolean result = service.evaluateContributingBetForReward(contribution);

        assertThat(result).isTrue(); // 10 < 20
    }

    @Test
    void evaluateContributingBetForReward_shouldReturnFalse_whenRandomGreaterThanChance() {
        Supplier<Double> fakeRandom = () -> 90.0; // deterministic random
        FixedRewardEvaluationTypeService service = new FixedRewardEvaluationTypeService(fakeRandom);

        Jackpot jackpot = mock(Jackpot.class);
        when(jackpot.getInitialRewardChancePercentage()).thenReturn(20.0);

        JackpotContribution contribution = mock(JackpotContribution.class);
        when(contribution.getJackpot()).thenReturn(jackpot);

        boolean result = service.evaluateContributingBetForReward(contribution);

        assertThat(result).isFalse(); // 90 > 20
    }

    @Test
    void evaluateContributingBetForReward_shouldReturnTrue_whenRandomEqualsZero() {
        Supplier<Double> fakeRandom = () -> 0.0; // edge case
        FixedRewardEvaluationTypeService service = new FixedRewardEvaluationTypeService(fakeRandom);

        Jackpot jackpot = mock(Jackpot.class);
        when(jackpot.getInitialRewardChancePercentage()).thenReturn(1.0);

        JackpotContribution contribution = mock(JackpotContribution.class);
        when(contribution.getJackpot()).thenReturn(jackpot);

        boolean result = service.evaluateContributingBetForReward(contribution);

        assertThat(result).isTrue(); // 0 < 1
    }
}
