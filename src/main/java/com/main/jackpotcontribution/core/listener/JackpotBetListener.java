package com.main.jackpotcontribution.core.listener;


import com.main.jackpotcontribution.core.domain.dto.JackpotBetDTO;
import com.main.jackpotcontribution.core.domain.kafka.event.JackpotBetSavedEvent;
import com.main.jackpotcontribution.core.service.JackpotBetThreadService;
import com.main.jackpotcontribution.core.service.JackpotManagingService;
import com.main.jackpotcontribution.core.service.JackpotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.main.jackpotcontribution.core.domain.constants.KafkaConstants.JACKPOT_BET_TOPIC;

@Component
public class JackpotBetListener {
    private final static Logger logger = LoggerFactory.getLogger(JackpotBetListener.class);

    final private JackpotManagingService jackpotManagingService;
    final private JackpotService jackpotService;
    final private JackpotBetThreadService jackpotBetThreadService;

    public JackpotBetListener(JackpotManagingService jackpotManagingService,
                              JackpotService jackpotService,
                              JackpotBetThreadService jackpotBetThreadService) {
        this.jackpotManagingService = jackpotManagingService;
        this.jackpotService = jackpotService;
        this.jackpotBetThreadService = jackpotBetThreadService;
    }

    @KafkaListener(topics = JACKPOT_BET_TOPIC)
    public void handleJackpotBet(JackpotBetSavedEvent event) {
        if (jackpotService.existsById(event.jackpotId())) {
            final JackpotBetDTO jackpotBetDto = new JackpotBetDTO(event.betId(), event.userId(), event.jackpotId(), event.betAmount());

            jackpotBetThreadService.getExecutorServiceByJackpotId(event.jackpotId())
                    .execute(() -> jackpotManagingService.handleContribution(
                                    jackpotBetDto,
                                    jackpotService.findJackpotById(event.jackpotId())
                            )
                    );
        } else {
            logger.info("Could not find jackpot with id = {} and status IN_PROGRESS to contribute to for bet with id = {}...", event.jackpotId(), event.betId());
        }
    }

}
