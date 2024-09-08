package com.imjustdoom.minecrashservice.dto.out.model;

public record SimpleSolutionDto(int id, String error) {

    public static SimpleSolutionDto create(int id, String error) {
        return new SimpleSolutionDto(id, error);
    }
}
