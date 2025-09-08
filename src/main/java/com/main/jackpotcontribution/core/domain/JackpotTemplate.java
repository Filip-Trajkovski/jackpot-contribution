package com.main.jackpotcontribution.core.domain;


import com.main.jackpotcontribution.core.domain.enums.ContributionType;
import com.main.jackpotcontribution.core.domain.enums.RewardEvaluationType;
import jakarta.persistence.*;
import org.hibernate.annotations.Check;

@Entity
public class JackpotTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Check(constraints = "initial_contribution > 0")
    private Double initialContribution;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContributionType contributionType;

    @Column(nullable = false)
    @Check(constraints = "initial_contribution_percentage > 0")
    private Double initialContributionPercentage;

    @Column(nullable = false)
    @Check(constraints = "pool_limit > 0")
    private Double poolLimit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RewardEvaluationType rewardEvaluationType;

    @Column(nullable = false)
    private Double initialRewardChangePercentage;

    public JackpotTemplate() {
    }

    public RewardEvaluationType getRewardEvaluationType() {
        return rewardEvaluationType;
    }

    public Double getInitialRewardChangePercentage() {
        return initialRewardChangePercentage;
    }

    public void setInitialRewardChangePercentage(Double initialRewardChangePercentage) {
        this.initialRewardChangePercentage = initialRewardChangePercentage;
    }

    public void setRewardEvaluationType(RewardEvaluationType rewardEvaluationType) {
        this.rewardEvaluationType = rewardEvaluationType;
    }

    public void setContributionType(ContributionType contributionType) {
        this.contributionType = contributionType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getInitialContribution() {
        return initialContribution;
    }

    public void setInitialContribution(Double initialContribution) {
        this.initialContribution = initialContribution;
    }

    public ContributionType getContributionType() {
        return contributionType;
    }


    public Double getInitialContributionPercentage() {
        return initialContributionPercentage;
    }

    public void setInitialContributionPercentage(Double initialPercentage) {
        this.initialContributionPercentage = initialPercentage;
    }

    public Double getPoolLimit() {
        return poolLimit;
    }

    public void setPoolLimit(Double poolLimit) {
        this.poolLimit = poolLimit;
    }

}
