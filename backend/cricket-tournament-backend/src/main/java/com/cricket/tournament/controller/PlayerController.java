package com.cricket.tournament.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.cricket.tournament.dto.PlayerRequest;
import com.cricket.tournament.dto.PlayerResponse;
import com.cricket.tournament.service.PlayerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // Register a player under a team
    @PostMapping("/teams/{teamId}/players")
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerResponse createPlayer(
            @PathVariable Long teamId,
            @Valid @RequestBody PlayerRequest request) {

        return playerService.createPlayer(teamId, request);
    }

    // Retrieve all players belonging to a team
    @GetMapping("/teams/{teamId}/players")
    public List<PlayerResponse> getPlayersByTeam(
            @PathVariable Long teamId) {

        return playerService.getPlayersByTeam(teamId);
    }

    // Retrieve one player by ID
    @GetMapping("/players/{playerId}")
    public PlayerResponse getPlayerById(
            @PathVariable Long playerId) {

        return playerService.getPlayerById(playerId);
    }
}