package com.imjustdoom.minecrashservice.repository;

import com.imjustdoom.minecrashservice.model.StatisticsModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticsRepository extends JpaRepository<StatisticsModel, Integer> {
}
