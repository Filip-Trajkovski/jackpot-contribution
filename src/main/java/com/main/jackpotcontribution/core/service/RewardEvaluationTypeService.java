package com.main.jackpotcontribution.core.service;

import com.main.jackpotcontribution.core.domain.JackpotContribution;
import com.main.jackpotcontribution.core.domain.enums.RewardEvaluationType;

public interface RewardEvaluationTypeService {
    RewardEvaluationType getKey();

    boolean evaluateContributingBetForReward(JackpotContribution contribution);
}