package com.main.jackpotcontribution.core.domain;

import com.main.jackpotcontribution.core.domain.enums.JackpotContributionEvaluationStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.Check;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
public class JackpotContribution {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String betId;

    @Column(nullable = false)
    private String userId;

    @ManyToOne
    @JoinColumn(name = "jackpot_id", nullable = false)
    private Jackpot jackpot;

    @Column(nullable = false)
    @Check(constraints = "stake_amount > 0")
    private Double stakeAmount;

    @Column(nullable = false)
    @Check(constraints = "contribution_amount > 0")
    private Double contributionAmount;

    @Column(nullable = false)
    @Check(constraints = "current_jackpot_amount > 0")
    private Double currentJackpotAmount;

    @Column(nullable = false)
    private Timestamp createdAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JackpotContributionEvaluationStatus evaluationStatus;

    public JackpotContribution(String betId, String userId, Jackpot jackpot, Double stakeAmount,
                               Double contributionAmount, Double currentJackpotAmount) {
        this.betId = betId;
        this.userId = userId;
        this.jackpot = jackpot;
        this.stakeAmount = stakeAmount;
        this.contributionAmount = contributionAmount;
        this.currentJackpotAmount = currentJackpotAmount;
        this.evaluationStatus = JackpotContributionEvaluationStatus.UNEVALUATED;
        this.createdAt = Timestamp.from(Instant.now());
    }

    public JackpotContribution() {
    }

    public JackpotContributionEvaluationStatus getEvaluationStatus() {
        return evaluationStatus;
    }

    public void setEvaluationStatus(JackpotContributionEvaluationStatus evaluationStatus) {
        this.evaluationStatus = evaluationStatus;
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

    public Double getStakeAmount() {
        return stakeAmount;
    }

    public Double getContributionAmount() {
        return contributionAmount;
    }

    public Double getCurrentJackpotAmount() {
        return currentJackpotAmount;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
