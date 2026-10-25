package com.cricket.tournament.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cricket.tournament.dto.TeamRequest;
import com.cricket.tournament.dto.TeamResponse;
import com.cricket.tournament.entity.Team;
import com.cricket.tournament.entity.Tournament;
import com.cricket.tournament.exception.ResourceNotFoundException;
import com.cricket.tournament.repository.TeamRepository;
import com.cricket.tournament.repository.TournamentRepository;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;

    public TeamService(
            TeamRepository teamRepository,
            TournamentRepository tournamentRepository) {

        this.teamRepository = teamRepository;
        this.tournamentRepository = tournamentRepository;
    }

    // Create a team under an existing tournament
    @Transactional
    public TeamResponse createTeam(Long tournamentId, TeamRequest request) {

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tournament not found with ID: " + tournamentId));

        String teamName = request.getName().trim();

        boolean teamExists =
                teamRepository.existsByNameIgnoreCaseAndTournamentId(
                        teamName, tournamentId);

        if (teamExists) {
            throw new IllegalArgumentException(
                    "A team with this name already exists in the tournament");
        }

        Team team = new Team();

        team.setName(teamName);
        team.setShortName(request.getShortName().trim().toUpperCase());
        team.setHomeCity(request.getHomeCity().trim());
        team.setTournament(tournament);

        Team savedTeam = teamRepository.save(team);

        return convertToResponse(savedTeam);
    }

    // Retrieve all teams belonging to a tournament
    @Transactional(readOnly = true)
    public List<TeamResponse> getTeamsByTournament(Long tournamentId) {

        if (!tournamentRepository.existsById(tournamentId)) {
            throw new ResourceNotFoundException(
                    "Tournament not found with ID: " + tournamentId);
        }

        return teamRepository.findByTournamentId(tournamentId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Retrieve one team by ID
    @Transactional(readOnly = true)
    public TeamResponse getTeamById(Long teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Team not found with ID: " + teamId));

        return convertToResponse(team);
    }

    // Convert a Team entity into a TeamResponse DTO
    private TeamResponse convertToResponse(Team team) {

        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getShortName(),
                team.getHomeCity(),
                team.getTournament().getId(),
                team.getTournament().getName(),
                team.getMatchesPlayed(),
                team.getMatchesWon()
        );
    }
}