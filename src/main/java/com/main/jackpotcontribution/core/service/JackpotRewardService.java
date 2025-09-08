package com.main.jackpotcontribution.core.service;

import com.main.jackpotcontribution.core.domain.Jackpot;
import com.main.jackpotcontribution.core.domain.JackpotReward;
import com.main.jackpotcontribution.core.repository.JackpotRewardRepository;
import org.springframework.stereotype.Service;

@Service
public class JackpotRewardService {

    final private JackpotRewardRepository jackpotRewardRepository;

    public JackpotRewardService(JackpotRewardRepository jackpotRewardRepository) {
        this.jackpotRewardRepository = jackpotRewardRepository;
    }

    public JackpotReward save(String betId, String userId, Jackpot jackpot, Double jackpotRewardAmount) {
        return jackpotRewardRepository.save(new JackpotReward(betId, userId, jackpot, jackpotRewardAmount));
    }
}
