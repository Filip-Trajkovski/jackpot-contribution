package com.main.jackpotcontribution.core.service;


import com.main.jackpotcontribution.core.domain.JackpotContribution;
import com.main.jackpotcontribution.core.domain.enums.RewardEvaluationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

@Service
public class VariableRewardEvaluationTypeService implements RewardEvaluationTypeService {
    private static final Logger logger = LoggerFactory.getLogger(VariableRewardEvaluationTypeService.class);

    private final Supplier<Double> randomSupplier;

    public VariableRewardEvaluationTypeService() {
        this(() -> ThreadLocalRandom.current().nextDouble(0, 100));
    }

    public VariableRewardEvaluationTypeService(Supplier<Double> randomSupplier) {
        this.randomSupplier = randomSupplier;
    }

    @Override
    public RewardEvaluationType getKey() {
        return RewardEvaluationType.VARIABLE;
    }

    @Override
    public boolean evaluateContributingBetForReward(JackpotContribution contribution) {
        final Double variableRewardChancePercentage = getVariableRewardChancePercentageForJackpot(
                contribution.getJackpot().getInitialRewardChancePercentage(),
                contribution.getJackpot().getPoolLimit(),
                contribution.getJackpot().getTotalContribution());

        final double randomValue = randomSupplier.get();
        logger.info("Evaluating reward chance for JackpotContribution with id = {}, current reward chance = {}, comparing with random value = {}", contribution.getId(), variableRewardChancePercentage, randomValue);
        return randomValue <= variableRewardChancePercentage;
    }

    Double getVariableRewardChancePercentageForJackpot(Double initialRewardChancePercentage,
                                                       Double poolLimit,
                                                       Double currentTotalContribution) {
        final double poolFilledPercentage = (currentTotalContribution / poolLimit);

        return initialRewardChancePercentage + ((100 - initialRewardChancePercentage) * poolFilledPercentage);
    }
}
