package com.main.jackpotcontribution;

import com.main.jackpotcontribution.core.domain.Jackpot;
import com.main.jackpotcontribution.core.domain.JackpotContribution;
import com.main.jackpotcontribution.core.domain.dto.JackpotBetDTO;
import com.main.jackpotcontribution.core.domain.enums.ContributionType;
import com.main.jackpotcontribution.core.domain.enums.JackpotStatus;
import com.main.jackpotcontribution.core.domain.enums.RewardEvaluationType;
import com.main.jackpotcontribution.core.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JackpotManagingServiceTests {

    @Mock
    private JackpotService jackpotService;

    @Mock
    private JackpotContributionService jackpotContributionService;

    @Mock
    private ContributionTypeServiceCollector contributionTypeServiceCollector;

    @Mock
    private RewardEvaluationTypeServiceCollector rewardEvaluationTypeServiceCollector;

    @Mock
    private JackpotRewardService jackpotRewardService;

    @Mock
    private ContributionTypeService contributionTypeService;

    @Mock
    private RewardEvaluationTypeService rewardEvaluationTypeService;

    @InjectMocks
    private JackpotManagingService jackpotManagingService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleContribution_shouldSaveContribution_whenJackpotCanContribute() {
        Jackpot jackpot = mock(Jackpot.class);
        JackpotBetDTO betDTO = new JackpotBetDTO("bet-uuid", "user-uuid", "jackpot-uuid", 100.0);

        when(jackpot.canContribute()).thenReturn(true);
        when(jackpot.getId()).thenReturn("jackpot-uuid");
        when(jackpot.getContributionType()).thenReturn(ContributionType.FIXED);

        when(contributionTypeServiceCollector.getContributionTypeServiceByContributionType(ContributionType.FIXED))
                .thenReturn(contributionTypeService);
        when(contributionTypeService.evaluateContributionByBet(jackpot, 100.0)).thenReturn(50.0);
        when(jackpot.contributableAmountLeft()).thenReturn(100.0);

        Jackpot updatedJackpot = mock(Jackpot.class);
        when(updatedJackpot.getTotalContribution()).thenReturn(150.0);
        when(jackpotService.addContributionToJackpot(jackpot, 50.0)).thenReturn(updatedJackpot);

        jackpotManagingService.handleContribution(betDTO, jackpot);

        verify(jackpotService).addContributionToJackpot(jackpot, 50.0);
        verify(jackpotContributionService).save(
                "bet-uuid", "user-uuid", jackpot, 100.0, 50.0, 150.0
        );
    }

    @Test
    void handleContribution_shouldNotSaveContribution_whenJackpotCannotContribute() {
        Jackpot jackpot = mock(Jackpot.class);
        JackpotBetDTO betDTO = new JackpotBetDTO("bet-uuid", "user-uuid", "jackpot-uuid", 100.0);

        when(jackpot.canContribute()).thenReturn(false);
        when(jackpot.getId()).thenReturn("jackpot-uuid");

        jackpotManagingService.handleContribution(betDTO, jackpot);

        verifyNoInteractions(jackpotContributionService);
        verify(jackpotService, never()).addContributionToJackpot(any(), anyDouble());
    }

    @Test
    void getContributionForBet_shouldReturnContributableAmountLeft() {
        Jackpot jackpot = mock(Jackpot.class);
        when(jackpot.getContributionType()).thenReturn(ContributionType.VARIABLE);
        when(jackpot.getId()).thenReturn("jackpot-uuid");

        when(contributionTypeServiceCollector.getContributionTypeServiceByContributionType(ContributionType.VARIABLE))
                .thenReturn(contributionTypeService);
        when(contributionTypeService.evaluateContributionByBet(jackpot, 200.0)).thenReturn(80.0);
        when(jackpot.contributableAmountLeft()).thenReturn(50.0);

        Double result = jackpotManagingService.getContributionForBet(200.0, jackpot);

        assertThat(result).isEqualTo(50.0);
    }

    @Test
    void getContributionForBet_shouldReturnEvaluatedContribution() {
        Jackpot jackpot = mock(Jackpot.class);
        when(jackpot.getContributionType()).thenReturn(ContributionType.VARIABLE);
        when(jackpot.getId()).thenReturn("jackpot-uuid");

        when(contributionTypeServiceCollector.getContributionTypeServiceByContributionType(ContributionType.VARIABLE))
                .thenReturn(contributionTypeService);
        when(contributionTypeService.evaluateContributionByBet(jackpot, 200.0)).thenReturn(40.0);
        when(jackpot.contributableAmountLeft()).thenReturn(150.0);

        Double result = jackpotManagingService.getContributionForBet(200.0, jackpot);

        assertThat(result).isEqualTo(40.0);
    }

    @Test
    void evaluateContributingBetForReward_shouldGrantReward_whenWon() {
        Jackpot jackpot = mock(Jackpot.class);
        JackpotContribution contribution = mock(JackpotContribution.class);

        when(contribution.getJackpot()).thenReturn(jackpot);
        when(contribution.getId()).thenReturn("contribution-uuid");
        when(contribution.getBetId()).thenReturn("bet-uuid");
        when(contribution.getUserId()).thenReturn("user-uuid");

        when(jackpot.getStatus()).thenReturn(JackpotStatus.IN_PROGESS);
        when(jackpot.getRewardEvaluationType()).thenReturn(RewardEvaluationType.FIXED);
        when(jackpot.getTotalContribution()).thenReturn(500.0);
        when(jackpot.getId()).thenReturn("jackpot-uuid");

        when(rewardEvaluationTypeServiceCollector.getRewardEvaluationTypeServiceByRewardEvaluationType(RewardEvaluationType.FIXED))
                .thenReturn(rewardEvaluationTypeService);
        when(rewardEvaluationTypeService.evaluateContributingBetForReward(contribution)).thenReturn(true);

        Double result = jackpotManagingService.evaluateContributingBetForReward(contribution);

        assertThat(result).isEqualTo(500.0);
        verify(jackpotRewardService).save("bet-uuid", "user-uuid", jackpot, 500.0);
        verify(jackpotService).markJackpotAsGrantedAndCreateNewFromTemplate(jackpot);
    }

    @Test
    void evaluateContributingBetForReward_shouldReturnNull_whenLost() {
        Jackpot jackpot = mock(Jackpot.class);
        JackpotContribution contribution = mock(JackpotContribution.class);

        when(contribution.getJackpot()).thenReturn(jackpot);
        when(contribution.getId()).thenReturn("contribution-uuid");

        when(jackpot.getStatus()).thenReturn(JackpotStatus.IN_PROGESS);
        when(jackpot.getRewardEvaluationType()).thenReturn(RewardEvaluationType.VARIABLE);

        when(rewardEvaluationTypeServiceCollector.getRewardEvaluationTypeServiceByRewardEvaluationType(RewardEvaluationType.VARIABLE))
                .thenReturn(rewardEvaluationTypeService);
        when(rewardEvaluationTypeService.evaluateContributingBetForReward(contribution)).thenReturn(false);

        Double result = jackpotManagingService.evaluateContributingBetForReward(contribution);

        assertThat(result).isNull();
        verify(jackpotRewardService, never()).save(anyString(), anyString(), any(), anyDouble());
        verify(jackpotService, never()).markJackpotAsGrantedAndCreateNewFromTemplate(any());
    }

    @Test
    void evaluateContributingBetForReward_shouldReturnNull_whenJackpotAlreadyGranted() {
        Jackpot jackpot = mock(Jackpot.class);
        JackpotContribution contribution = mock(JackpotContribution.class);

        when(contribution.getJackpot()).thenReturn(jackpot);
        when(contribution.getId()).thenReturn("contribution-uuid");

        when(jackpot.getStatus()).thenReturn(JackpotStatus.GRANTED);
        when(jackpot.getId()).thenReturn("jackpot-uuid");

        Double result = jackpotManagingService.evaluateContributingBetForReward(contribution);

        assertThat(result).isNull();
        verifyNoInteractions(jackpotRewardService);
        verify(jackpotService, never()).markJackpotAsGrantedAndCreateNewFromTemplate(any());
    }
}
