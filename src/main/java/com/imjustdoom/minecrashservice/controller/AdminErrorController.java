package com.imjustdoom.minecrashservice.controller;

import com.imjustdoom.minecrashservice.dto.in.KnownErrorDto;
import com.imjustdoom.minecrashservice.model.SolutionModel;
import com.imjustdoom.minecrashservice.repository.SolutionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("error/admin")
public class AdminErrorController {

    private final SolutionRepository solutionRepository;

    public AdminErrorController(SolutionRepository solutionRepository) {
        this.solutionRepository = solutionRepository;
    }

    @PostMapping("new")
    public ResponseEntity<?> addNewKnownError(@RequestBody KnownErrorDto knownErrorDto) {
        SolutionModel solutionModel = new SolutionModel(knownErrorDto.error(), knownErrorDto.matches(), knownErrorDto.arguments(), knownErrorDto.solution());
        this.solutionRepository.save(solutionModel);
        return ResponseEntity.ok("Submitted new error");
    }
}
