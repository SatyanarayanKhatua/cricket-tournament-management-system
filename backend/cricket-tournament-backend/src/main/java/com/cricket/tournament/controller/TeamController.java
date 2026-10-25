package com.cricket.tournament.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.cricket.tournament.dto.TeamRequest;
import com.cricket.tournament.dto.TeamResponse;
import com.cricket.tournament.service.TeamService;

@RestController
@RequestMapping("/api")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    // Create a team under a tournament
    @PostMapping("/tournaments/{tournamentId}/teams")
    @ResponseStatus(HttpStatus.CREATED)
    public TeamResponse createTeam(
            @PathVariable Long tournamentId,
            @Valid @RequestBody TeamRequest request) {

        return teamService.createTeam(tournamentId, request);
    }

    // Get all teams belonging to a tournament
    @GetMapping("/tournaments/{tournamentId}/teams")
    public List<TeamResponse> getTeamsByTournament(
            @PathVariable Long tournamentId) {

        return teamService.getTeamsByTournament(tournamentId);
    }

    // Get a team by its ID
    @GetMapping("/teams/{teamId}")
    public TeamResponse getTeamById(
            @PathVariable Long teamId) {

        return teamService.getTeamById(teamId);
    }
}