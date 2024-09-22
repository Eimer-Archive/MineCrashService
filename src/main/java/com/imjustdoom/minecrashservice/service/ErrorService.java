package com.imjustdoom.minecrashservice.service;

import com.imjustdoom.minecrashservice.dto.out.ErrorSolutionDto;
import com.imjustdoom.minecrashservice.model.SolutionModel;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ErrorService {

    private final SolutionService solutionService;
    private final StatisticsService statisticsService;

    public ErrorService(SolutionService solutionService, StatisticsService statisticsService) {
        this.solutionService = solutionService;
        this.statisticsService = statisticsService;
    }

    public ErrorSolutionDto findSolution(String error, long start) {
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
            return ErrorSolutionDto.create(solutionModel.getError(), solution, System.currentTimeMillis() - start);
        }
        return null;
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
