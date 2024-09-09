package com.imjustdoom.minecrashservice.controller;

import com.imjustdoom.minecrashservice.dto.in.CheckErrorDto;
import com.imjustdoom.minecrashservice.dto.out.*;
import com.imjustdoom.minecrashservice.service.ErrorService;
import com.imjustdoom.minecrashservice.service.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ErrorController as in the Controller for handling user submitted errors
 */
@RestController
@RequestMapping("error")
public class ErrorController {

    private final ErrorService errorService;
    private final StatisticsService statisticsService;

    public ErrorController(ErrorService errorService, StatisticsService statisticsService) {
        this.errorService = errorService;
        this.statisticsService = statisticsService;
    }

    @PostMapping("check")
    public ResponseEntity<?> checkError(@RequestBody CheckErrorDto checkErrorDto) {
        if (checkErrorDto.error().getBytes().length > 24 * 1024 * 1024) {
            return ResponseEntity.badRequest().body(ErrorDto.create("Error length is too long, max length is 10240"));
        }

        ErrorSolutionDto solution = this.errorService.findSolution(checkErrorDto.error());
        if (solution == null) {
            return ResponseEntity.ok(ResponseDto.create("Unable to solve this error, it has been submitted to the database for review. " +
                    "Try updating any server software (Paper, Purpur etc), mods, plugins or the Minecraft version itself." +
                    " Or include more information in the log file, there may not be enough information in the current log file. " +
                    "Please join the discord server to provide more context or help solve it. Here is the reference id \"" +
                    this.errorService.getSubmittedErrorIdByError(checkErrorDto.error()).orElseGet(() ->
                            this.errorService.submitError(checkErrorDto.error())).getId() + "\"."));
        }

        return ResponseEntity.ok(solution);
    }

    @GetMapping("statistics")
    public ResponseEntity<?> getStatistics() {
        return ResponseEntity.ok(StatisticsDto.create(this.statisticsService.getSolvedErrors(), this.errorService.getSubmittedCount()));
    }
}
