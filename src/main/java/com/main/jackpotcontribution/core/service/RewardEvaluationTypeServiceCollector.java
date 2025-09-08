package com.main.jackpotcontribution.core.service;

import com.main.jackpotcontribution.core.domain.enums.RewardEvaluationType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RewardEvaluationTypeServiceCollector {

    final private Map<RewardEvaluationType, RewardEvaluationTypeService> rewardEvaluationTypeServiceMap;

    public RewardEvaluationTypeServiceCollector(List<RewardEvaluationTypeService> rewardEvaluationTypeServiceMap) {
        this.rewardEvaluationTypeServiceMap = rewardEvaluationTypeServiceMap.stream().collect(
                Collectors.toMap(RewardEvaluationTypeService::getKey, service -> service)
        );
    }

    public RewardEvaluationTypeService getRewardEvaluationTypeServiceByRewardEvaluationType(RewardEvaluationType rewardEvaluationType) {
        return rewardEvaluationTypeServiceMap.get(rewardEvaluationType);
    }
}
