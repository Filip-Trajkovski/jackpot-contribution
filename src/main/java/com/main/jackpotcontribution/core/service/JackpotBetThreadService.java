package com.main.jackpotcontribution.core.service;

import com.main.jackpotcontribution.core.domain.enums.JackpotStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class JackpotBetThreadService {
    private final static Logger logger = LoggerFactory.getLogger(JackpotBetThreadService.class);

    private final ConcurrentHashMap<String, ExecutorService> jackpotExecutorServiceMap;
    private final JackpotService jackpotService;

    public JackpotBetThreadService(JackpotService jackpotService) {
        this.jackpotExecutorServiceMap = new ConcurrentHashMap<>();
        this.jackpotService = jackpotService;
    }

    public ExecutorService getExecutorServiceByJackpotId(String id) {
        return jackpotExecutorServiceMap.computeIfAbsent(id, k -> Executors.newSingleThreadExecutor());
    }

    @Scheduled(fixedRate = 60000)
    private void clearExecutorServicesOfGrantedJackpots() {
        jackpotService.findAllByIdInAndStatus(jackpotExecutorServiceMap.keySet(), JackpotStatus.GRANTED).forEach(
                jackpot -> {
                    logger.info("Removing ExecutorService for jackpot with id = {}...", jackpot.getId());
                    jackpotExecutorServiceMap.get(jackpot.getId()).shutdown();
                    jackpotExecutorServiceMap.remove(jackpot.getId());
                });
    }

}
