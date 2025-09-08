package com.main.jackpotcontribution.core.service;

import com.main.jackpotcontribution.core.domain.Jackpot;
import com.main.jackpotcontribution.core.domain.enums.ContributionType;

public interface ContributionTypeService {

    ContributionType getKey();

    Double evaluateContributionByBet(Jackpot jackpot, Double betAmount);
}
