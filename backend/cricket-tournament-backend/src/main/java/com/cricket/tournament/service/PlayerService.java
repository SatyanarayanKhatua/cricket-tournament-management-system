package com.cricket.tournament.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cricket.tournament.dto.PlayerRequest;
import com.cricket.tournament.dto.PlayerResponse;
import com.cricket.tournament.entity.Player;
import com.cricket.tournament.entity.Team;
import com.cricket.tournament.exception.ResourceNotFoundException;
import com.cricket.tournament.repository.PlayerRepository;
import com.cricket.tournament.repository.TeamRepository;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public PlayerService(
            PlayerRepository playerRepository,
            TeamRepository teamRepository) {

        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    // Register a player under an existing team
    @Transactional
    public PlayerResponse createPlayer(Long teamId, PlayerRequest request) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Team not found with ID: " + teamId));

        Player player = new Player();

        player.setName(request.getName().trim());
        player.setRole(request.getRole().trim().toUpperCase());
        player.setBattingStyle(
                request.getBattingStyle().trim().toUpperCase());

        String bowlingStyle = request.getBowlingStyle();

        player.setBowlingStyle(
                bowlingStyle == null || bowlingStyle.isBlank()
                        ? null
                        : bowlingStyle.trim().toUpperCase());

        player.setTeam(team);

        Player savedPlayer = playerRepository.save(player);

        return convertToResponse(savedPlayer);
    }

    // Retrieve all players belonging to a particular team
    @Transactional(readOnly = true)
    public List<PlayerResponse> getPlayersByTeam(Long teamId) {

        if (!teamRepository.existsById(teamId)) {
            throw new ResourceNotFoundException(
                    "Team not found with ID: " + teamId);
        }

        return playerRepository.findByTeamId(teamId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Retrieve a player by ID
    @Transactional(readOnly = true)
    public PlayerResponse getPlayerById(Long playerId) {

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Player not found with ID: " + playerId));

        return convertToResponse(player);
    }

    // Convert a Player entity into a PlayerResponse DTO
    private PlayerResponse convertToResponse(Player player) {

        return new PlayerResponse(
                player.getId(),
                player.getName(),
                player.getRole(),
                player.getBattingStyle(),
                player.getBowlingStyle(),
                player.getTeam().getId(),
                player.getTeam().getName()
        );
    }
}