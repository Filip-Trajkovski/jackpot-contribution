package com.main.jackpotcontribution.core.service;

import com.main.jackpotcontribution.core.domain.Jackpot;
import com.main.jackpotcontribution.core.domain.JackpotContribution;
import com.main.jackpotcontribution.core.domain.enums.JackpotContributionEvaluationStatus;
import com.main.jackpotcontribution.core.repository.JackpotContributionRepository;
import org.springframework.stereotype.Service;

@Service
public class JackpotContributionService {

    final private JackpotContributionRepository jackpotContributionRepository;

    public JackpotContributionService(JackpotContributionRepository jackpotContributionRepository) {
        this.jackpotContributionRepository = jackpotContributionRepository;
    }

    public JackpotContribution save(String betId, String userId, Jackpot jackpot,
                                    Double stakeAmount, Double contributionAmount, Double currentJackpotAmount) {
        return jackpotContributionRepository.save(
                new JackpotContribution(betId, userId, jackpot, stakeAmount,
                        contributionAmount, currentJackpotAmount)
        );
    }

    public JackpotContribution findById(String id) {
        return jackpotContributionRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Could not find JackpotContribution with id: " + id + "...")
        );
    }

    public JackpotContribution setEvaluationStatus(JackpotContribution jackpotContribution,
                                                   JackpotContributionEvaluationStatus evaluationStatus) {
        jackpotContribution.setEvaluationStatus(evaluationStatus);
        return jackpotContributionRepository.save(jackpotContribution);
    }

}
