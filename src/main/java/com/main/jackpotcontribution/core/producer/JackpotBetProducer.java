package com.main.jackpotcontribution.core.producer;

import com.main.jackpotcontribution.core.domain.dto.JackpotBetDTO;
import com.main.jackpotcontribution.core.domain.kafka.event.JackpotBetSavedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.main.jackpotcontribution.core.domain.constants.KafkaConstants.JACKPOT_BET_TOPIC;

@Component
public class JackpotBetProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public JackpotBetProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishJackpotBet(JackpotBetDTO jackpotBet) {
        kafkaTemplate.send(JACKPOT_BET_TOPIC, new JackpotBetSavedEvent(
                jackpotBet.betId(), jackpotBet.userId(), jackpotBet.jackpotId(), jackpotBet.betAmount()
        ));
    }

}
