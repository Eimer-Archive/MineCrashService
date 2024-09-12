package com.imjustdoom.minecrashservice.repository;

import com.imjustdoom.minecrashservice.model.SettingsModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<SettingsModel, Integer> {
}
