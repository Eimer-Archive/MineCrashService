package com.imjustdoom.minecrashservice.model;

import jakarta.persistence.*;

@Entity(name = "settings")
public class SettingsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String lastSolutionsZipHash;

    public String getLastSolutionsZipHash() {
        return this.lastSolutionsZipHash;
    }

    public void setLastSolutionsZipHash(String lastSolutionsZipHash) {
        this.lastSolutionsZipHash = lastSolutionsZipHash;
    }
}
