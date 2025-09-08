package com.main.jackpotcontribution.api.controller;


import com.main.jackpotcontribution.api.request.JackpotBetRequest;
import com.main.jackpotcontribution.api.response.RewardResponse;
import com.main.jackpotcontribution.core.domain.JackpotContribution;
import com.main.jackpotcontribution.core.domain.dto.JackpotBetDTO;
import com.main.jackpotcontribution.core.producer.JackpotBetProducer;
import com.main.jackpotcontribution.core.service.JackpotBetThreadService;
import com.main.jackpotcontribution.core.service.JackpotContributionService;
import com.main.jackpotcontribution.core.service.JackpotManagingService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("api/jackpot-bet")
public class JackpotBetController {

    private final JackpotManagingService jackpotManagingService;
    private final JackpotBetProducer jackpotBetProducer;
    private final JackpotContributionService jackpotContributionService;
    private final JackpotBetThreadService jackpotBetThreadService;

    public JackpotBetController(JackpotManagingService jackpotManagingService,
                                JackpotBetProducer jackpotBetProducer,
                                JackpotContributionService jackpotContributionService,
                                JackpotBetThreadService jackpotBetThreadService) {
        this.jackpotManagingService = jackpotManagingService;
        this.jackpotBetProducer = jackpotBetProducer;
        this.jackpotContributionService = jackpotContributionService;
        this.jackpotBetThreadService = jackpotBetThreadService;
    }

    @PostMapping
    public void publishJackpotBet(@RequestBody JackpotBetRequest jackpotBetRequest) {
        jackpotBetProducer.publishJackpotBet(
                new JackpotBetDTO(
                        jackpotBetRequest.betId(),
                        jackpotBetRequest.userId(),
                        jackpotBetRequest.jackpotId(),
                        jackpotBetRequest.betAmount()
                )
        );
    }

    @PostMapping("/evaluate-for-reward")
    public RewardResponse evaluateContributingBetForReward(@RequestParam String jackpotContributionId) throws ExecutionException, InterruptedException {
        final JackpotContribution jackpotContribution = jackpotContributionService.findById(jackpotContributionId);
        final Double reward = jackpotBetThreadService.getExecutorServiceByJackpotId(jackpotContribution.getJackpot().getId())
                .submit(
                        () -> jackpotManagingService.evaluateContributingBetForReward(jackpotContributionService.findById(jackpotContributionId))
                ).get();

        return new RewardResponse(reward);
    }

}
