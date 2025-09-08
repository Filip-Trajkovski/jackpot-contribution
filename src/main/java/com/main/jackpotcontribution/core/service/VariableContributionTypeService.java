package com.main.jackpotcontribution.core.service;

import com.main.jackpotcontribution.core.domain.Jackpot;
import com.main.jackpotcontribution.core.domain.enums.ContributionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class VariableContributionTypeService implements ContributionTypeService {
    private static final Logger logger = LoggerFactory.getLogger(VariableContributionTypeService.class);

    @Override
    public ContributionType getKey() {
        return ContributionType.VARIABLE;
    }

    @Override
    public Double evaluateContributionByBet(Jackpot jackpot, Double betAmount) {
        logger.info("Calculating variable contribution for jackpot with id = {}, current totalContribution = {}," +
                " poolLimit = {}, initialContributionPercentage = {} and betAmount = {}...", jackpot.getId(), jackpot.getTotalContribution(), jackpot.getPoolLimit(), jackpot.getInitialContributionPercentage(), betAmount);

        final Double variablePercentage = calculateVariablePercentage(jackpot.getTotalContribution(), jackpot.getPoolLimit(), jackpot.getInitialContributionPercentage());

        return (betAmount * variablePercentage) / 100;
    }

    private Double calculateVariablePercentage(Double poolCurrentContribution, Double poolLimit, Double initialPercentage) {
        final double poolFilledPercentage = (poolCurrentContribution / poolLimit) * 100;
        final int fivePercentSteps = (int) Math.floor(poolFilledPercentage / 5);

        return initialPercentage * (1 - 0.05 * fivePercentSteps);
    }
}
