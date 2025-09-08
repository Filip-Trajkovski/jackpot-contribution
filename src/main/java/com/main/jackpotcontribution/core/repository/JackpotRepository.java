package com.main.jackpotcontribution.core.repository;

import com.main.jackpotcontribution.core.domain.Jackpot;
import com.main.jackpotcontribution.core.domain.enums.JackpotStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface JackpotRepository extends JpaRepository<Jackpot, String> {
    List<Jackpot> findAllByIdInAndStatus(Set<String> ids, JackpotStatus status);

    boolean existsById(String id);
}
