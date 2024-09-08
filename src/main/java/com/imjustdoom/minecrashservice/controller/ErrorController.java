package com.imjustdoom.minecrashservice.controller;

import com.imjustdoom.minecrashservice.dto.in.CheckErrorDto;
import com.imjustdoom.minecrashservice.dto.out.ErrorDto;
import com.imjustdoom.minecrashservice.dto.out.ErrorSolutionDto;
import com.imjustdoom.minecrashservice.dto.out.CountDto;
import com.imjustdoom.minecrashservice.dto.out.ResponseDto;
import com.imjustdoom.minecrashservice.service.ErrorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ErrorController as in the Controller for handling user submitted errors
 */
@RestController
@RequestMapping("error")
public class ErrorController {

    private final ErrorService errorService;

    public ErrorController(ErrorService errorService) {
        this.errorService = errorService;
    }

    @PostMapping("check")
    public ResponseEntity<?> checkError(@RequestBody CheckErrorDto checkErrorDto) {
        if (checkErrorDto.error().length() > 10240) {
            return ResponseEntity.badRequest().body(ErrorDto.create("Error length is too long, max length is 10240"));
        }

        ErrorSolutionDto solution = this.errorService.findSolution(checkErrorDto.error());
        if (solution == null) {
            return ResponseEntity.ok(ResponseDto.create("Unable to solve this error, it has been submitted to the database for review. " +
                    "Try updating any server software (Paper, Purpur etc), mods, plugins or the Minecraft version itself. " +
                    "Please join the discord server to provide more context or help solve it. Here is the reference id \"" +
                    this.errorService.getSubmittedErrorIdByError(checkErrorDto.error()).orElseGet(() ->
                            this.errorService.submitError(checkErrorDto.error())).getId() + "\"."));
        }

        return ResponseEntity.ok(solution);
    }

    @GetMapping("submittedCount")
    public ResponseEntity<?> getSubmittedSize() {
        return ResponseEntity.ok(CountDto.create(this.errorService.getSubmittedCount()));
    }
}
