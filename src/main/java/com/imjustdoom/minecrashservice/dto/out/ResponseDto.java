package com.imjustdoom.minecrashservice.dto.out;

public record ResponseDto(String response, long took) {

    public static ResponseDto create(String response, long took) {
        return new ResponseDto(response, took);
    }
}
