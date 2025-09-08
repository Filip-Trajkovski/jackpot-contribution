package com.main.jackpotcontribution.core.repository;

import com.main.jackpotcontribution.core.domain.JackpotReward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JackpotRewardRepository extends JpaRepository<JackpotReward, String> {
}
