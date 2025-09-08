package com.main.jackpotcontribution.core.service;

import com.main.jackpotcontribution.core.domain.JackpotTemplate;
import com.main.jackpotcontribution.core.repository.JackpotTemplateRepository;
import org.springframework.stereotype.Service;

@Service
public class JackpotTemplateService {

    private final JackpotTemplateRepository repository;

    public JackpotTemplateService(JackpotTemplateRepository repository) {
        this.repository = repository;
    }

    public JackpotTemplate findById(String id) {
        return repository.findById(id).orElseThrow(
                () -> new RuntimeException("Could not find JackpotTemplate with id: " + id + "...")
        );
    }
}
