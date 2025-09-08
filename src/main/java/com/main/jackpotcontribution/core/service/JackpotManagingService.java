package com.main.jackpotcontribution.core.service;

import com.main.jackpotcontribution.core.domain.Jackpot;
import com.main.jackpotcontribution.core.domain.JackpotContribution;
import com.main.jackpotcontribution.core.domain.dto.JackpotBetDTO;
import com.main.jackpotcontribution.core.domain.enums.JackpotContributionEvaluationStatus;
import com.main.jackpotcontribution.core.domain.enums.JackpotStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JackpotManagingService {
    private final static Logger logger = LoggerFactory.getLogger(JackpotManagingService.class);

    final private JackpotService jackpotService;
    final private JackpotContributionService jackpotContributionService;
    final private ContributionTypeServiceCollector contributionTypeServiceCollector;
    final private RewardEvaluationTypeServiceCollector rewardEvaluationTypeServiceCollector;
    final private JackpotRewardService jackpotRewardService;


    public JackpotManagingService(JackpotService jackpotService, JackpotContributionService jackpotContributionService,
                                  ContributionTypeServiceCollector contributionTypeServiceCollector,
                                  RewardEvaluationTypeServiceCollector rewardEvaluationTypeServiceCollector,
                                  JackpotRewardService jackpotRewardService) {
        this.jackpotService = jackpotService;
        this.jackpotContributionService = jackpotContributionService;
        this.contributionTypeServiceCollector = contributionTypeServiceCollector;
        this.rewardEvaluationTypeServiceCollector = rewardEvaluationTypeServiceCollector;
        this.jackpotRewardService = jackpotRewardService;
    }

    @Transactional
    public void handleContribution(JackpotBetDTO betDTO, Jackpot jackpot) {
        if (jackpot.canContribute()) {
            final Double actualContributionAmount = getContributionForBet(betDTO.betAmount(), jackpot);
            logger.info("{} to be contributed to jackpot with id = {} from bet with id = {}...", actualContributionAmount, jackpot.getId(), betDTO.betId());

            final Jackpot updatedJackpot = jackpotService.addContributionToJackpot(jackpot, actualContributionAmount);

            jackpotContributionService.save(
                    betDTO.betId(), betDTO.userId(), jackpot, betDTO.betAmount(), actualContributionAmount,
                    updatedJackpot.getTotalContribution());
        } else {
            logger.info("Could not contribute to jackpot with id = {} as contribution rules are not fulfilled...", jackpot.getId());
        }
    }

    public Double getContributionForBet(Double betAmount, Jackpot jackpot) {
        final ContributionTypeService contributionTypeService = contributionTypeServiceCollector.getContributionTypeServiceByContributionType(jackpot.getContributionType());
        final Double betContributionAmount = contributionTypeService.evaluateContributionByBet(jackpot, betAmount);
        final Double contributableAmountLeft = jackpot.contributableAmountLeft();
        logger.info("Evaluated betContributionAmount = {}, while contributableAmountLeft = {} for jackpot with id = {}...", betContributionAmount, contributableAmountLeft, jackpot.getId());

        return contributableAmountLeft > betContributionAmount ? betContributionAmount : contributableAmountLeft;
    }

    @Transactional
    public Double evaluateContributingBetForReward(JackpotContribution jackpotContribution) {
        if (jackpotContribution.getEvaluationStatus() == JackpotContributionEvaluationStatus.EVALUATED) {
            throw new RuntimeException("The JackpotContribution with id =" + jackpotContribution.getId() + "has already been evaluated!");
        }

        if (jackpotContribution.getJackpot().getStatus() == JackpotStatus.IN_PROGESS) {
            final RewardEvaluationTypeService service = rewardEvaluationTypeServiceCollector.getRewardEvaluationTypeServiceByRewardEvaluationType(jackpotContribution.getJackpot().getRewardEvaluationType());
            final boolean rewardWon = service.evaluateContributingBetForReward(jackpotContribution);
            jackpotContributionService.setEvaluationStatus(jackpotContribution, JackpotContributionEvaluationStatus.EVALUATED);

            if (rewardWon) {
                logger.info("JackpotContribution with id = {} has won the jackpot with id = {} reward! Total amount won = {}...",
                        jackpotContribution.getId(), jackpotContribution.getJackpot().getId(), jackpotContribution.getJackpot().getTotalContribution());

                jackpotRewardService.save(jackpotContribution.getBetId(), jackpotContribution.getUserId(),
                        jackpotContribution.getJackpot(), jackpotContribution.getJackpot().getTotalContribution());

                jackpotService.markJackpotAsGrantedAndCreateNewFromTemplate(jackpotContribution.getJackpot());

                return jackpotContribution.getJackpot().getTotalContribution();
            } else {
                logger.info("JackpotContribution with id = {} has been evaluated for reward with a negative result...", jackpotContribution.getId());

                return null;
            }
        } else {
            logger.info("The jackpot with id = {} for JackpotContribution with id = {} has already been granted...", jackpotContribution.getJackpot().getId(), jackpotContribution.getId());

            return null;
        }
    }

}
