package com.imjustdoom.minecrashservice.service;

import com.imjustdoom.minecrashservice.dto.out.ErrorSolutionDto;
import com.imjustdoom.minecrashservice.model.SolutionModel;
import com.imjustdoom.minecrashservice.model.SubmittedErrorModel;
import com.imjustdoom.minecrashservice.repository.SubmittedErrorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ErrorService {

    private final SubmittedErrorRepository errorRepository;

    private final SolutionService solutionService;
    private final StatisticsService statisticsService;

    public ErrorService(SubmittedErrorRepository errorRepository, SolutionService solutionService, StatisticsService statisticsService) {
        this.errorRepository = errorRepository;

        this.solutionService = solutionService;
        this.statisticsService = statisticsService;
    }

    public ErrorSolutionDto findSolution(String error) {
        for (SolutionModel solutionModel : this.solutionService.getAllSolutions()) {

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
            if (!matches) {
                continue;
            }

            String solution = solutionModel.getSolution();
            for (String arg : solutionModel.getArguments().keySet()) {
                Matcher matcher = Pattern.compile(solutionModel.getArguments().get(arg)).matcher(error); // TODO: Allow multiple possibly ways if the initial one fails

                if (matcher.find()) {
                    for (int i = 1; i <= matcher.groupCount(); i++) {
                        if (matcher.group(i) == null) continue;
                        solution = solution.replaceAll("%" + arg, Matcher.quoteReplacement(matcher.group(i)));
                    }
                } else {
                    solution = solution.replaceAll("%" + arg, "(Unable to find extra info, try providing more information in the error)");
                }
            }

            this.statisticsService.incrementSolvedErrors();
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

    /**
     * Get the submitted error by its error
     * @param error
     * @return Optional SubmittedErrorModel
     */
    public Optional<SubmittedErrorModel> getSubmittedErrorIdByError(String error) {
        return this.errorRepository.findByError(error);
    }

    /**
     * Get how many errors are in line for review
     * @return the errors in line for review as a long
     */
    public long getSubmittedCount() {
        return this.errorRepository.count();
    }

    /**
     * Replace any IPv4 addresses with 'x.x.x.x'
     * @param string
     * @return
     */
    public String replaceIps(String string) {
        return string.replaceAll("(?<!\\d)(?:(?:25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\\.){3}(?:25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])(?!\\d)", "x.x.x.x");
    }
}
