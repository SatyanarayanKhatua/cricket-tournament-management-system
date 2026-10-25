package com.cricket.tournament.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.cricket.tournament.dto.MatchRequest;
import com.cricket.tournament.dto.MatchResponse;
import com.cricket.tournament.service.MatchService;

import com.cricket.tournament.dto.OversRevisionRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    // Schedule a match under a tournament
    @PostMapping("/tournaments/{tournamentId}/matches")
    @ResponseStatus(HttpStatus.CREATED)
    public MatchResponse createMatch(
            @PathVariable Long tournamentId,
            @Valid @RequestBody MatchRequest request) {

        return matchService.createMatch(tournamentId, request);
    }

    // Retrieve all matches belonging to a tournament
    @GetMapping("/tournaments/{tournamentId}/matches")
    public List<MatchResponse> getMatchesByTournament(
            @PathVariable Long tournamentId) {

        return matchService.getMatchesByTournament(tournamentId);
    }

    // Retrieve one match by ID
    @GetMapping("/matches/{matchId}")
    public MatchResponse getMatchById(
            @PathVariable Long matchId) {

        return matchService.getMatchById(matchId);
    }
    
 // Optional: Reduce overs due to rain, bad light, or another interruption
    @PatchMapping("/matches/{matchId}/overs")
    public MatchResponse reviseOvers(
            @PathVariable Long matchId,
            @Valid @RequestBody OversRevisionRequest request) {

        return matchService.reviseOvers(matchId, request);
    }
}