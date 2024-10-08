package com.imjustdoom.minecrashservice.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Map;

@Entity(name = "solution")
public class SolutionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String error;
    @ElementCollection
    @CollectionTable(name = "matches", joinColumns = @JoinColumn(name = "error_solution_id"))
    @Column(name = "match_value")
    private List<String> matches;

    @ElementCollection
    @CollectionTable(name = "arguments", joinColumns = @JoinColumn(name = "error_solution_id"))
    @MapKeyColumn(name = "argument_key")
    @Column(name = "argument_value")
    private Map<String, String> arguments;
    @Column(length = 1024)
    private String solution;

    public SolutionModel() {}

    public SolutionModel(String error, List<String> matches, Map<String, String> arguments, String solution) {
        this.error = error;
        this.matches = matches;
        this.arguments = arguments;
        this.solution = solution;
    }

    public Integer getId() {
        return this.id;
    }

    public String getError() {
        return this.error;
    }

    public List<String> getMatches() {
        return this.matches;
    }

    public Map<String, String> getArguments() {
        return this.arguments;
    }

    public String getSolution() {
        return this.solution;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setMatches(List<String> matches) {
        this.matches = matches;
    }

    public void setArguments(Map<String, String> arguments) {
        this.arguments = arguments;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
}
