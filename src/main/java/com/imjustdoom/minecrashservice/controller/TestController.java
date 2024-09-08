package com.imjustdoom.minecrashservice.controller;

import com.imjustdoom.minecrashservice.dto.out.TestDto;
import com.imjustdoom.minecrashservice.dto.out.TestListDto;
import com.imjustdoom.minecrashservice.model.Test;
import com.imjustdoom.minecrashservice.repository.TestRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class TestController {

    private final TestRepo testRepo;

    public TestController(TestRepo testRepo) {
        this.testRepo = testRepo;
    }

    @GetMapping("create")
    public TestDto test() {
        Test test = new Test("brud");
        this.testRepo.saveAndFlush(test);
        return TestDto.create(test.getId(), test.getName());
    }

    @GetMapping("size")
    public long getSize() {
        return this.testRepo.count();
    }

    @GetMapping("all")
    public TestListDto all() {
        return TestListDto.create(this.testRepo.findAll().stream().map(test -> TestDto.create(test.getId(), test.getName())).toList());
    }
}
