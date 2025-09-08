package com.main.jackpotcontribution;

import com.main.jackpotcontribution.core.domain.Jackpot;
import com.main.jackpotcontribution.core.domain.JackpotTemplate;
import com.main.jackpotcontribution.core.domain.enums.JackpotStatus;
import com.main.jackpotcontribution.core.repository.JackpotRepository;
import com.main.jackpotcontribution.core.service.JackpotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class JackpotServiceTests {

    @Mock
    private JackpotRepository jackpotRepository;

    @InjectMocks
    private JackpotService jackpotService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addContributionToJackpot_shouldAddContribution_whenValid() {
        Jackpot jackpot = mock(Jackpot.class);

        when(jackpot.getStatus()).thenReturn(JackpotStatus.IN_PROGESS);
        when(jackpot.getId()).thenReturn("jackpot-uuid");
        when(jackpot.contributableAmountLeft()).thenReturn(100.0);
        when(jackpot.getTotalContribution()).thenReturn(50.0);

        // Simulate new value after setTotalContribution
        doAnswer(invocation -> {
            Double newValue = invocation.getArgument(0);
            when(jackpot.getTotalContribution()).thenReturn(newValue);
            return null;
        }).when(jackpot).setTotalContribution(anyDouble());

        when(jackpotRepository.save(jackpot)).thenReturn(jackpot);

        Jackpot result = jackpotService.addContributionToJackpot(jackpot, 40.0);

        assertThat(result.getTotalContribution()).isEqualTo(90.0);
        verify(jackpot).setTotalContribution(90.0);
        verify(jackpotRepository).save(jackpot);
    }

    @Test
    void addContributionToJackpot_shouldThrow_whenJackpotNotInProgress() {
        Jackpot jackpot = mock(Jackpot.class);

        when(jackpot.getStatus()).thenReturn(JackpotStatus.GRANTED);
        when(jackpot.getId()).thenReturn("jackpot-uuid");

        assertThatThrownBy(() -> jackpotService.addContributionToJackpot(jackpot, 50.0))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not in status IN_PROGESS");
    }

    @Test
    void addContributionToJackpot_shouldThrow_whenContributionTooHigh() {
        Jackpot jackpot = mock(Jackpot.class);

        when(jackpot.getStatus()).thenReturn(JackpotStatus.IN_PROGESS);
        when(jackpot.getId()).thenReturn("jackpot-uuid");
        when(jackpot.contributableAmountLeft()).thenReturn(30.0);

        assertThatThrownBy(() -> jackpotService.addContributionToJackpot(jackpot, 50.0))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("more than the limit");
    }

    @Test
    void markJackpotAsGrantedAndCreateNewFromTemplate_shouldUpdateAndCreateNew() {
        Jackpot jackpot = mock(Jackpot.class);
        JackpotTemplate template = mock(JackpotTemplate.class);
        Jackpot newJackpot = mock(Jackpot.class);

        when(jackpot.getJackpotTemplate()).thenReturn(template);
        when(template.getId()).thenReturn("template-uuid");
        when(jackpotRepository.save(any(Jackpot.class))).thenReturn(newJackpot);

        Jackpot result = jackpotService.markJackpotAsGrantedAndCreateNewFromTemplate(jackpot);

        assertThat(result).isEqualTo(newJackpot);
        verify(jackpot).setStatus(JackpotStatus.GRANTED);
        verify(jackpotRepository, atLeastOnce()).save(any(Jackpot.class));
    }
}
