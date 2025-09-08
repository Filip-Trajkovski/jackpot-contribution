package com.main.jackpotcontribution.core.service;

import com.main.jackpotcontribution.core.domain.JackpotContribution;
import com.main.jackpotcontribution.core.domain.enums.RewardEvaluationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

@Service
public class FixedRewardEvaluationTypeService implements RewardEvaluationTypeService {
    private static final Logger logger = LoggerFactory.getLogger(FixedRewardEvaluationTypeService.class);

    private final Supplier<Double> randomSupplier;

    public FixedRewardEvaluationTypeService() {
        this(() -> ThreadLocalRandom.current().nextDouble(0, 100));
    }

    public FixedRewardEvaluationTypeService(Supplier<Double> randomSupplier) {
        this.randomSupplier = randomSupplier;
    }

    @Override
    public RewardEvaluationType getKey() {
        return RewardEvaluationType.FIXED;
    }

    @Override
    public boolean evaluateContributingBetForReward(JackpotContribution contribution) {
        double randomValue = randomSupplier.get();
        double jackpotRewardChancePercentage = contribution.getJackpot().getInitialRewardChancePercentage();

        logger.info(
                "Evaluating reward chance for JackpotContribution with id = {}, current reward chance = {}, comparing with random value = {}",
                contribution.getId(), jackpotRewardChancePercentage, randomValue
        );

        return randomValue <= jackpotRewardChancePercentage;
    }
}
