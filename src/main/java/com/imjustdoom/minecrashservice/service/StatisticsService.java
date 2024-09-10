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

    /**
     * Get the statistics model
     * @return
     */
    public StatisticsModel getStatistics() {
        return this.statisticsRepository.findById(1).orElseGet(StatisticsModel::new);
    }

    /**
     * How the statistic for how many errors have been solved
     * @return
     */
    public long getSolvedErrors() {
        return getStatistics().getSolvedErrors();
    }

    /**
     * Increments the amount of solved errors
     */
    public void incrementSolvedErrors() {
        this.statisticsRepository.save(getStatistics().incrementSolvedErrors());
    }
}
