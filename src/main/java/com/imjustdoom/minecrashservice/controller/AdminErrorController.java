package com.imjustdoom.minecrashservice.controller;

import com.imjustdoom.minecrashservice.dto.in.AddKnownErrorDto;
import com.imjustdoom.minecrashservice.dto.in.ModifyKnownErrorDto;
import com.imjustdoom.minecrashservice.dto.out.model.SimpleSolutionDto;
import com.imjustdoom.minecrashservice.model.SolutionModel;
import com.imjustdoom.minecrashservice.repository.SolutionRepository;
import com.imjustdoom.minecrashservice.service.ErrorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("error/admin")
public class AdminErrorController {

    private final SolutionRepository solutionRepository;

    private final ErrorService errorService;

    public AdminErrorController(SolutionRepository solutionRepository, ErrorService errorService) {
        this.solutionRepository = solutionRepository;
        this.errorService = errorService;
    }

    @PostMapping("new")
    public ResponseEntity<?> addNewKnownError(@RequestBody AddKnownErrorDto addKnownErrorDto) {
        SolutionModel solutionModel = new SolutionModel(addKnownErrorDto.error(), addKnownErrorDto.matches(), addKnownErrorDto.arguments(), addKnownErrorDto.solution());
        this.solutionRepository.save(solutionModel);
        return ResponseEntity.ok("Submitted new error");
    }

    @PostMapping("modify")
    public ResponseEntity<?> modifySolution(@RequestBody ModifyKnownErrorDto modifyKnownErrorDto) {
        System.out.println(modifyKnownErrorDto);
        boolean success = this.errorService.modifySolution(modifyKnownErrorDto);

        if (success) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("solutions")
    public ResponseEntity<Page<SimpleSolutionDto>> getSolutions(@PageableDefault(size = 25, sort = "error") final Pageable pageable) {
        Page<SimpleSolutionDto> page = this.solutionRepository.findAll(pageable).map(solution -> SimpleSolutionDto.create(solution.getId(), solution.getError()));
        return ResponseEntity.ok(page);
    }
}
