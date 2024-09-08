package com.imjustdoom.minecrashservice.dto.out;

public record ResponseDto(String response) {

    public static ResponseDto create(String response) {
        return new ResponseDto(response);
    }
}
