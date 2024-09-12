package com.imjustdoom.minecrashservice.service;

import com.imjustdoom.minecrashservice.dto.in.ModifyKnownErrorDto;
import com.imjustdoom.minecrashservice.model.SolutionModel;
import com.imjustdoom.minecrashservice.repository.SolutionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SolutionService {

    private final SolutionRepository solutionRepository;

    public SolutionService(SolutionRepository solutionRepository) {
        this.solutionRepository = solutionRepository;
    }

    public Page<SolutionModel> getAllSolutions(Pageable pageable) {
        return this.solutionRepository.findAll(pageable);
    }

    public List<SolutionModel> getAllSolutions() {
        return this.solutionRepository.findAll();
    }

    public void addNewSolution(String error, List<String> matches, Map<String, String> arguments, String solution) {
        addNewSolution(new SolutionModel(error, matches, arguments, solution));
    }

    public void addNewSolution(SolutionModel solutionModel) {
        this.solutionRepository.save(solutionModel);
    }

    /**
     * Modify a submitted solution
     * @param modifyKnownErrorDto
     * @return
     */
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

    public void clearAll() {
        this.solutionRepository.deleteAll();
    }
}
