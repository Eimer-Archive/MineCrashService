package com.imjustdoom.minecrashservice.model;

import jakarta.persistence.*;

@Entity
public class StatisticsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private long solvedErrors;

    public StatisticsModel() {
        this.solvedErrors = 0;
    }

    public long getSolvedErrors() {
        return this.solvedErrors;
    }

    public StatisticsModel incrementSolvedErrors() {
        this.solvedErrors++;
        return this;
    }
}
