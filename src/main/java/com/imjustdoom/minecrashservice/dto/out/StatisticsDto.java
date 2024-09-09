package com.imjustdoom.minecrashservice.dto.out;

public record StatisticsDto(long solvedErrors, long errorsForReview) {

    public static StatisticsDto create(long solvedErrors, long errorsForReview) {
        return new StatisticsDto(solvedErrors, errorsForReview);
    }
}
