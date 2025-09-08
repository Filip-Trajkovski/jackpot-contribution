package com.main.jackpotcontribution.core.service;

import com.main.jackpotcontribution.core.domain.Jackpot;
import com.main.jackpotcontribution.core.domain.JackpotTemplate;
import com.main.jackpotcontribution.core.domain.enums.JackpotStatus;
import com.main.jackpotcontribution.core.repository.JackpotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class JackpotService {
    private static final Logger logger = LoggerFactory.getLogger(JackpotService.class);

    final private JackpotRepository jackpotRepository;

    public JackpotService(JackpotRepository jackpotRepository) {
        this.jackpotRepository = jackpotRepository;
    }

    public Jackpot findJackpotById(String id) {
        return jackpotRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Could not find Jackpot with id = {" + id + "}...")
        );
    }

    public boolean existsById(String id) {
        return jackpotRepository.existsById(id);
    }

    public List<Jackpot> findAllByIdInAndStatus(Set<String> ids, JackpotStatus status) {
        return jackpotRepository.findAllByIdInAndStatus(ids, status);
    }

    public Jackpot addContributionToJackpot(Jackpot jackpot, Double amount) {
        if (jackpot.getStatus() != JackpotStatus.IN_PROGESS)
            throw new RuntimeException("You cannot contribute to jackpot with id " + jackpot.getId() + " because it is not in status IN_PROGESS...");

        if (amount > jackpot.contributableAmountLeft())
            throw new RuntimeException("You tried to contribute more than the limit for jackpot with id = " + jackpot.getId() + "...");

        logger.info("Adding {} to jackpot with id = {} with current total contribution = {}", amount, jackpot.getId(), jackpot.getTotalContribution());

        jackpot.setTotalContribution(jackpot.getTotalContribution() + amount);
        return jackpotRepository.save(jackpot);
    }

    @Transactional
    public Jackpot markJackpotAsGrantedAndCreateNewFromTemplate(Jackpot jackpot) {
        changeJackpotStatus(jackpot, JackpotStatus.GRANTED);

        return createJackpotFromTemplate(jackpot.getJackpotTemplate());
    }

    private void changeJackpotStatus(Jackpot jackpot, JackpotStatus status) {
        jackpot.setStatus(status);
        jackpotRepository.save(jackpot);
    }

    private Jackpot createJackpotFromTemplate(JackpotTemplate jackpotTemplate) {
        logger.info("Creating jackpot from jackpot template with id = {}...", jackpotTemplate.getId());

        return jackpotRepository.save(new Jackpot(jackpotTemplate));
    }
}
