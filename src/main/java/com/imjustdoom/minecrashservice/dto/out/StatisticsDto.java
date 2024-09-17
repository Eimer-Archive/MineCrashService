package com.imjustdoom.minecrashservice.dto.out;

public record StatisticsDto(long solvedErrors) {

    public static StatisticsDto create(long solvedErrors) {
        return new StatisticsDto(solvedErrors);
    }
}
