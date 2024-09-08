package com.imjustdoom.minecrashservice.repository;

import com.imjustdoom.minecrashservice.model.SolutionModel;
import com.imjustdoom.minecrashservice.model.SubmittedErrorModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SolutionRepository extends JpaRepository<SolutionModel, Integer> {
}