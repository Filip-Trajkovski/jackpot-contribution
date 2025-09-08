package com.main.jackpotcontribution.core.domain;


import jakarta.persistence.*;
import org.hibernate.annotations.Check;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
public class JackpotReward {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String betId;

    @Column(nullable = false)
    private String userId;

    @OneToOne
    @JoinColumn(name = "jackpot_id", unique = true)
    private Jackpot jackpot;

    @Column(nullable = false)
    @Check(constraints = "jackpot_reward_amount > 0")
    private Double jackpotRewardAmount;

    @Column(nullable = false)
    private Timestamp createdAtDate;

    public JackpotReward(String betId, String userId, Jackpot jackpot, Double jackpotRewardAmount) {
        this.betId = betId;
        this.userId = userId;
        this.jackpot = jackpot;
        this.jackpotRewardAmount = jackpotRewardAmount;
        this.createdAtDate = Timestamp.from(Instant.now());
    }

    public JackpotReward() {
    }

    public String getId() {
        return id;
    }

    public String getBetId() {
        return betId;
    }

    public String getUserId() {
        return userId;
    }

    public Jackpot getJackpot() {
        return jackpot;
    }

    public Double getJackpotRewardAmount() {
        return jackpotRewardAmount;
    }

    public Timestamp getCreatedAtDate() {
        return createdAtDate;
    }
}
