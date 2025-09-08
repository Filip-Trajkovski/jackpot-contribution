package com.main.jackpotcontribution.core.service;


import com.main.jackpotcontribution.core.domain.Jackpot;
import com.main.jackpotcontribution.core.domain.enums.ContributionType;
import org.springframework.stereotype.Service;

@Service
public class FixedContributionTypeService implements ContributionTypeService {

    @Override
    public ContributionType getKey() {
        return ContributionType.FIXED;
    }

    @Override
    public Double evaluateContributionByBet(Jackpot jackpot, Double betAmount) {
        return (betAmount * jackpot.getInitialContributionPercentage()) / 100;
    }
}
