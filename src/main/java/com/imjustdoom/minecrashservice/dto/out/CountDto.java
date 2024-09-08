package com.imjustdoom.minecrashservice.dto.out;

public record CountDto(long count) {

    public static CountDto create(long count) {
        return new CountDto(count);
    }
}
