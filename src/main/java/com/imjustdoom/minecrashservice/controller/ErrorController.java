package com.imjustdoom.minecrashservice.controller;

import com.imjustdoom.minecrashservice.dto.in.CheckErrorDto;
import com.imjustdoom.minecrashservice.dto.out.*;
import com.imjustdoom.minecrashservice.service.ErrorService;
import com.imjustdoom.minecrashservice.service.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
        // Make sure the input text is not larger than 12MiB
        if (checkErrorDto.error().getBytes().length > 12 * 1024 * 1024) {
            return ResponseEntity.badRequest().body(ErrorDto.create("Error length is too long, max file size is "));
        }

        long start = System.currentTimeMillis();
        ErrorSolutionDto solution = this.errorService.findSolution(checkErrorDto.error(), start);
        return ResponseEntity.ok(Objects.requireNonNullElseGet(solution, () -> ResponseDto.create(
                "Unable to solve this error. Try updating any server software"
                + " (Paper, Purpur etc), mods, plugins you have installed or the Minecraft version itself. Or include"
                + " more information in the log file, there may not be enough information in the current log file for"
                + " this to be able to solve it. Otherwise please ask for support in a suitable place like the PaperMC,"
                + " Purpur, your plugin/mod if you know it is causing the issue, etc discord server or forums."
                + " If you find the solution please submit it for us to add to the database"
                + " [here](https://github.com/Eimer-Archive/MineCrashSolutions?tab=readme-ov-file#requesting-a-solution-to-be-added)!",
                System.currentTimeMillis() - start
        )));

    }

    @GetMapping("statistics")
    public ResponseEntity<?> getStatistics() {
        return ResponseEntity.ok(StatisticsDto.create(this.statisticsService.getSolvedErrors()));
    }
}
