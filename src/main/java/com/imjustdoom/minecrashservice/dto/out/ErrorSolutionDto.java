package com.imjustdoom.minecrashservice.dto.out;

public record ErrorSolutionDto(String title, String solution, long took) {

    public static ErrorSolutionDto create(String title, String solution, long took) {
        return new ErrorSolutionDto(title, solution, took);
    }
}
