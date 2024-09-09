package com.imjustdoom.minecrashservice.service;

import com.imjustdoom.minecrashservice.model.StatisticsModel;
import com.imjustdoom.minecrashservice.repository.StatisticsRepository;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    public StatisticsService(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    public StatisticsModel getStatistics() {
        return this.statisticsRepository.findById(1).orElseGet(StatisticsModel::new);
    }

    public long getSolvedErrors() {
        return getStatistics().getSolvedErrors();
    }

    public void incrementSolvedErrors() {
        this.statisticsRepository.save(getStatistics().incrementSolvedErrors());
    }
}
