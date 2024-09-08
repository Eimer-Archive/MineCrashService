package com.imjustdoom.minecrashservice.dto.out;

public record ErrorSolutionDto(String solution) {

    public static ErrorSolutionDto create(String solution) {
        return new ErrorSolutionDto(solution);
    }
}
