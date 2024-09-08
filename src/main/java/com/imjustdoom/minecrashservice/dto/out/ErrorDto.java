package com.imjustdoom.minecrashservice.dto.out;

public record ErrorDto(String error) {

    public static ErrorDto create(String error) {
        return new ErrorDto(error);
    }
}
