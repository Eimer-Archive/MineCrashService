package com.imjustdoom.minecrashservice.service;

import com.imjustdoom.minecrashservice.model.SettingsModel;
import com.imjustdoom.minecrashservice.repository.SettingsRepository;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    private final SettingsRepository settingsRepository;

    public SettingsService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    /**
     * Get the statistics model
     * @return
     */
    public SettingsModel getSettings() {
        return this.settingsRepository.findById(1).orElseGet(SettingsModel::new);
    }

    public String getZipHash() {
        return getSettings().getLastSolutionsZipHash();
    }

    public void updateZipHash(String hash) {
        SettingsModel settingsModel = getSettings();
        settingsModel.setLastSolutionsZipHash(hash);
        this.settingsRepository.saveAndFlush(settingsModel);
    }
}
