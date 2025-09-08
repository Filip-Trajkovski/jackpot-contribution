package com.main.jackpotcontribution.core.repository;

import com.main.jackpotcontribution.core.domain.JackpotContribution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JackpotContributionRepository extends JpaRepository<JackpotContribution, String> {
}
