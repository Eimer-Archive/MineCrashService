package com.imjustdoom.minecrashservice.dto.out;

public record TestDto(long id, String name) {

    public static TestDto create(long id, String name) {
        return new TestDto(id, name);
    }
}
