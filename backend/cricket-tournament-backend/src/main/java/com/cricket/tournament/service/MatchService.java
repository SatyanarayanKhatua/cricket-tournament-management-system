package com.cricket.tournament.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cricket.tournament.dto.MatchRequest;
import com.cricket.tournament.dto.MatchResponse;
import com.cricket.tournament.entity.Match;
import com.cricket.tournament.entity.Team;
import com.cricket.tournament.entity.Tournament;
import com.cricket.tournament.exception.ResourceNotFoundException;
import com.cricket.tournament.repository.MatchRepository;
import com.cricket.tournament.repository.TeamRepository;
import com.cricket.tournament.repository.TournamentRepository;

import com.cricket.tournament.dto.OversRevisionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;

    public MatchService(
            MatchRepository matchRepository,
            TeamRepository teamRepository,
            TournamentRepository tournamentRepository) {

        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.tournamentRepository = tournamentRepository;
    }

    // Schedule a match under a tournament
    @Transactional
    public MatchResponse createMatch(Long tournamentId, MatchRequest request) {

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tournament not found with ID: " + tournamentId));

        Team team1 = teamRepository.findById(request.getTeam1Id())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Team 1 not found with ID: " + request.getTeam1Id()));

        Team team2 = teamRepository.findById(request.getTeam2Id())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Team 2 not found with ID: " + request.getTeam2Id()));

        // A team cannot play against itself
        if (team1.getId().equals(team2.getId())) {
            throw new IllegalArgumentException(
                    "A team cannot play against itself");
        }

        // Both teams must belong to the selected tournament
        if (!team1.getTournament().getId().equals(tournamentId)
                || !team2.getTournament().getId().equals(tournamentId)) {
            throw new IllegalArgumentException(
                    "Both teams must belong to the selected tournament");
        }

        String format = request.getFormat().trim().toUpperCase();

        // Match format must agree with the tournament format
        if (!tournament.getFormat().trim().equalsIgnoreCase(format)) {
            throw new IllegalArgumentException(
                    "Match format must match the tournament format: "
                            + tournament.getFormat());
        }

        Integer overs = request.getScheduledOvers();

        // Validate overs according to format
        switch (format) {
            case "T20":
                if (overs == null || overs < 1 || overs > 20) {
                    throw new IllegalArgumentException(
                            "T20 matches must have between 1 and 20 scheduled overs");
                }
                break;

            case "ODI":
                if (overs == null || overs < 1 || overs > 50) {
                    throw new IllegalArgumentException(
                            "ODI matches must have between 1 and 50 scheduled overs");
                }
                break;

            case "TEST":
                if (overs != null) {
                    throw new IllegalArgumentException(
                            "Test matches do not use a fixed overs limit");
                }
                break;

            case "CUSTOM":
                if (overs == null || overs < 1) {
                    throw new IllegalArgumentException(
                            "Custom matches require a positive overs limit");
                }
                break;

            default:
                throw new IllegalArgumentException(
                        "Unsupported format. Use T20, ODI, TEST, or CUSTOM");
        }

        Match match = new Match();

        match.setMatchName(request.getMatchName().trim());
        match.setFormat(format);
        match.setVenue(request.getVenue().trim());
        match.setMatchDateTime(request.getMatchDateTime());
        match.setStatus("SCHEDULED");

        match.setTournament(tournament);
        match.setTeam1(team1);
        match.setTeam2(team2);

        match.setScheduledOvers(overs);
        match.setCurrentOvers(overs);

        Match savedMatch = matchRepository.save(match);

        return convertToResponse(savedMatch);
    }

    // Retrieve all matches in a tournament
    @Transactional(readOnly = true)
    public List<MatchResponse> getMatchesByTournament(Long tournamentId) {

        if (!tournamentRepository.existsById(tournamentId)) {
            throw new ResourceNotFoundException(
                    "Tournament not found with ID: " + tournamentId);
        }

        return matchRepository.findByTournamentId(tournamentId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Retrieve one match by ID
    @Transactional(readOnly = true)
    public MatchResponse getMatchById(Long matchId) {

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Match not found with ID: " + matchId));

        return convertToResponse(match);
    }

    // Convert the database entity into a response DTO

    private MatchResponse convertToResponse(Match match) {

        return new MatchResponse(
                match.getId(),
                match.getMatchName(),
                match.getFormat(),
                match.getVenue(),
                match.getMatchDateTime(),
                match.getStatus(),
                match.getTournament().getId(),
                match.getTournament().getName(),
                match.getTeam1().getId(),
                match.getTeam1().getName(),
                match.getTeam2().getId(),
                match.getTeam2().getName(),
                match.getScheduledOvers(),
                match.getCurrentOvers(),
                match.getOversRevisionReason(),
                match.getWinner() != null
                        ? match.getWinner().getId()
                        : null,
                match.getWinner() != null
                        ? match.getWinner().getName()
                        : null,
                match.getResultType(),
                match.getResultSummary()
        );
    }
 

 // Optional: Reduce the overs limit when weather interrupts a match
    @Transactional
    public MatchResponse reviseOvers(
            Long matchId,
            OversRevisionRequest request) {

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Match not found with ID: " + matchId));

        // Only scheduled or live matches can have their overs revised
        if (!match.getStatus().equals("SCHEDULED")
                && !match.getStatus().equals("LIVE")) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Overs cannot be revised for a completed or cancelled match");
        }

        Integer currentOvers = match.getCurrentOvers();
        Integer revisedOvers = request.getRevisedOvers();

        if (currentOvers == null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This match does not use a fixed overs limit");
        }

        // Overs can only be reduced through this endpoint
        if (revisedOvers >= currentOvers) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Revised overs must be lower than the current overs limit");
        }

        String reason = request.getReason().trim().toUpperCase();

        if (!reason.equals("RAIN")
                && !reason.equals("BAD_LIGHT")
                && !reason.equals("OTHER")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Reason must be RAIN, BAD_LIGHT, or OTHER");
        }

        match.setCurrentOvers(revisedOvers);
        match.setOversRevisionReason(reason);

        Match updatedMatch = matchRepository.save(match);

        return convertToResponse(updatedMatch);
    }
}