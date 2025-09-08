package com.main.jackpotcontribution.core.service;

import com.main.jackpotcontribution.core.domain.enums.ContributionType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ContributionTypeServiceCollector {

    final private Map<ContributionType, ContributionTypeService> contrutionTypeServiceMap;

    public ContributionTypeServiceCollector(List<ContributionTypeService> contributionTypeServiceList) {
        this.contrutionTypeServiceMap = contributionTypeServiceList.stream().collect(
                Collectors.toMap(ContributionTypeService::getKey, service -> service)
        );
    }

    public ContributionTypeService getContributionTypeServiceByContributionType(ContributionType contributionType) {
        return contrutionTypeServiceMap.get(contributionType);
    }
}
