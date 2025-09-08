package com.main.jackpotcontribution.core.domain;


import com.main.jackpotcontribution.core.domain.enums.ContributionType;
import com.main.jackpotcontribution.core.domain.enums.JackpotStatus;
import com.main.jackpotcontribution.core.domain.enums.RewardEvaluationType;
import jakarta.persistence.*;
import org.hibernate.annotations.Check;

@Entity
public class Jackpot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "jackpot_template_id", nullable = false)
    private JackpotTemplate jackpotTemplate;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContributionType contributionType;

    @Column(nullable = false)
    @Check(constraints = "initial_contribution_percentage > 0")
    private Double initialContributionPercentage;

    @Column(nullable = false)
    @Check(constraints = "total_contribution > 0")
    private Double totalContribution;

    @Column(nullable = false)
    @Check(constraints = "pool_limit > 0")
    private Double poolLimit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RewardEvaluationType rewardEvaluationType;

    @Column(nullable = false)
    @Check(constraints = "initial_reward_chance_percentage > 0")
    private Double initialRewardChancePercentage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JackpotStatus status;

    public Jackpot(JackpotTemplate jackpotTemplate) {
        this.jackpotTemplate = jackpotTemplate;
        this.name = jackpotTemplate.getName();
        this.contributionType = jackpotTemplate.getContributionType();
        this.initialContributionPercentage = jackpotTemplate.getInitialContributionPercentage();
        this.totalContribution = jackpotTemplate.getInitialContribution();
        this.poolLimit = jackpotTemplate.getPoolLimit();
        this.rewardEvaluationType = jackpotTemplate.getRewardEvaluationType();
        this.initialRewardChancePercentage = jackpotTemplate.getInitialRewardChangePercentage();
        this.status = JackpotStatus.IN_PROGESS;
    }

    public Jackpot() {
    }

    public JackpotStatus getStatus() {
        return status;
    }

    public void setStatus(JackpotStatus status) {
        this.status = status;
    }

    public RewardEvaluationType getRewardEvaluationType() {
        return rewardEvaluationType;
    }

    public Double getInitialRewardChancePercentage() {
        return initialRewardChancePercentage;
    }

    public Double getTotalContribution() {
        return totalContribution;
    }

    public void setTotalContribution(Double totalValue) {
        this.totalContribution = totalValue;
    }

    public String getId() {
        return id;
    }

    public JackpotTemplate getJackpotTemplate() {
        return jackpotTemplate;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ContributionType getContributionType() {
        return contributionType;
    }

    public Double getInitialContributionPercentage() {
        return initialContributionPercentage;
    }

    public Double getPoolLimit() {
        return poolLimit;
    }

    public boolean canContribute() {
        return totalContribution < poolLimit && status == JackpotStatus.IN_PROGESS;
    }

    public Double contributableAmountLeft() {
        if (canContribute()) {
            return poolLimit - totalContribution;
        } else {
            return 0.0;
        }
    }
}
