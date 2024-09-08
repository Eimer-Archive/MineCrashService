package com.imjustdoom.minecrashservice.dto.out;

public record ErrorSolutionDto(String title, String solution) {

    public static ErrorSolutionDto create(String title, String solution) {
        return new ErrorSolutionDto(title, solution);
    }
}
