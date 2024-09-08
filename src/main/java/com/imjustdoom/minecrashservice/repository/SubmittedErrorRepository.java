package com.imjustdoom.minecrashservice.repository;

import com.imjustdoom.minecrashservice.model.SubmittedErrorModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubmittedErrorRepository extends JpaRepository<SubmittedErrorModel, Integer> {

    Optional<SubmittedErrorModel> findByError(String error);
}
