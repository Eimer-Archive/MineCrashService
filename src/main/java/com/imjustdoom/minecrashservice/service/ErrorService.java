package com.imjustdoom.minecrashservice.service;

import com.imjustdoom.minecrashservice.dto.in.ModifyKnownErrorDto;
import com.imjustdoom.minecrashservice.dto.out.ErrorSolutionDto;
import com.imjustdoom.minecrashservice.model.SolutionModel;
import com.imjustdoom.minecrashservice.model.SubmittedErrorModel;
import com.imjustdoom.minecrashservice.repository.SolutionRepository;
import com.imjustdoom.minecrashservice.repository.SubmittedErrorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ErrorService {

    private final SubmittedErrorRepository errorRepository;
    private final SolutionRepository solutionRepository;

    public ErrorService(SubmittedErrorRepository errorRepository, SolutionRepository solutionRepository) {
        this.errorRepository = errorRepository;
        this.solutionRepository = solutionRepository;
    }

    public ErrorSolutionDto findSolution(String error) {
        for (SolutionModel solutionModel : this.solutionRepository.findAll()) {

            // Matches system is an "or"
            // Maybe make a system to specify "or"/"and" for matches
            boolean matches = false;
            for (String match : solutionModel.getMatches()) {
                if (!error.contains(match)) {
                    continue;
                }
                matches = true;
                break;
            }
            if (!matches) continue;

            String solution = solutionModel.getSolution();
            for (String arg : solutionModel.getArguments().keySet()) {
                Matcher matcher = Pattern.compile(solutionModel.getArguments().get(arg)).matcher(error);

                if (matcher.find()) {
                    solution = solution.replaceAll("%" + arg, matcher.group(1));
                }

                solution = solution.replaceAll("%" + arg, "(Unable to find extra info)");
            }

            return ErrorSolutionDto.create(solutionModel.getError(), solution);
        }
        return null;
    }

    /**
     * Checks if the table already contains this exact error
     *
     * @param error
     * @return
     */
    public boolean containsError(String error) {
        return this.errorRepository.findByError(error).isPresent();
    }

    /**
     * Submit the error to the database
     *
     * @param error
     * @return the object
     */
    public SubmittedErrorModel submitError(String error) {
        SubmittedErrorModel submittedErrorModel = new SubmittedErrorModel(replaceIps(error));
        return this.errorRepository.save(submittedErrorModel);
    }

    public Optional<SubmittedErrorModel> getSubmittedErrorIdByError(String error) {
        return this.errorRepository.findByError(error);
    }

    public boolean modifySolution(ModifyKnownErrorDto modifyKnownErrorDto) {
        Optional<SolutionModel> optionalSolutionModel = this.solutionRepository.findById(modifyKnownErrorDto.id());
        if (optionalSolutionModel.isEmpty()) {
            return false;
        }
        SolutionModel solution = optionalSolutionModel.get();

        if (modifyKnownErrorDto.error() != null && !modifyKnownErrorDto.error().isBlank()) {
            solution.setError(modifyKnownErrorDto.error());
        }

        if (modifyKnownErrorDto.solution() != null && !modifyKnownErrorDto.solution().isBlank()) {
            solution.setSolution(modifyKnownErrorDto.solution());
        }

        if (modifyKnownErrorDto.matches() != null && !modifyKnownErrorDto.matches().isEmpty()) {
            solution.setMatches(modifyKnownErrorDto.matches());
        }

        if (modifyKnownErrorDto.arguments() != null && !modifyKnownErrorDto.arguments().isEmpty()) {
            solution.setArguments(modifyKnownErrorDto.arguments());
        }
        this.solutionRepository.save(solution);

        return true;
    }

    public long getSubmittedCount() {
        return this.errorRepository.count();
    }

    public String replaceIps(String string) {
        return string.replaceAll("(?<!\\d)(?:(?:25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\\.){3}(?:25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])(?!\\d)", "x.x.x.x");
    }
}
