package com.imjustdoom.minecrashservice.dto.out;

import java.util.List;

public record TestListDto(List<TestDto> list) {

    public static TestListDto create(List<TestDto> list) {
        return new TestListDto(list);
    }
}
