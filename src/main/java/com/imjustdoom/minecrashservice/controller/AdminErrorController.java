package com.imjustdoom.minecrashservice.controller;

import com.imjustdoom.minecrashservice.dto.in.AddKnownErrorDto;
import com.imjustdoom.minecrashservice.dto.in.ModifyKnownErrorDto;
import com.imjustdoom.minecrashservice.dto.out.model.SimpleSolutionDto;
import com.imjustdoom.minecrashservice.service.SolutionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("error/admin")
public class AdminErrorController {

    private final SolutionService solutionService;

    public AdminErrorController(SolutionService solutionService) {
        this.solutionService = solutionService;
    }

    @PostMapping("new")
    public ResponseEntity<?> addNewKnownError(@RequestBody AddKnownErrorDto addKnownErrorDto) {
        this.solutionService.addNewSolution(addKnownErrorDto.error(), addKnownErrorDto.matches(), addKnownErrorDto.arguments(), addKnownErrorDto.solution());
        return ResponseEntity.ok("Submitted new error");
    }

    @PostMapping("modify")
    public ResponseEntity<?> modifySolution(@RequestBody ModifyKnownErrorDto modifyKnownErrorDto) {
        boolean success = this.solutionService.modifySolution(modifyKnownErrorDto);

        if (success) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("solutions")
    public ResponseEntity<Page<SimpleSolutionDto>> getSolutions(@PageableDefault(size = 25, sort = "error") final Pageable pageable) {
        Page<SimpleSolutionDto> page = this.solutionService.getAllSolutions(pageable).map(solution -> SimpleSolutionDto.create(solution.getId(), solution.getError()));
        return ResponseEntity.ok(page);
    }
}
