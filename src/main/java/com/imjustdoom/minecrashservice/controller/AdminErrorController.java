package com.imjustdoom.minecrashservice.controller;

import com.imjustdoom.minecrashservice.dto.in.KnownErrorDto;
import com.imjustdoom.minecrashservice.dto.out.model.SimpleSolutionDto;
import com.imjustdoom.minecrashservice.model.SolutionModel;
import com.imjustdoom.minecrashservice.repository.SolutionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("solutions")
    public ResponseEntity<Page<SimpleSolutionDto>> getSolutions(@PageableDefault(size = 25, sort = "error") final Pageable pageable) {
        Page<SimpleSolutionDto> page = this.solutionRepository.findAll(pageable).map(solution -> SimpleSolutionDto.create(solution.getId(), solution.getError()));
        return ResponseEntity.ok(page);
    }
}
