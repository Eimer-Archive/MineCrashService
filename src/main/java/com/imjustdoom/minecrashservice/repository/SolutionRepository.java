package com.imjustdoom.minecrashservice.repository;

import com.imjustdoom.minecrashservice.model.SolutionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolutionRepository extends JpaRepository<SolutionModel, Integer> {
}
