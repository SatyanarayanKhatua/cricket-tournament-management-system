package com.cricket.tournament.service;

import com.cricket.tournament.dto.InningsRequest;
import com.cricket.tournament.entity.Player;
import com.cricket.tournament.repository.PlayerRepository;
import com.cricket.tournament.dto.InningsResponse;
import com.cricket.tournament.entity.Innings;
import com.cricket.tournament.entity.Match;
import com.cricket.tournament.entity.Team;
import com.cricket.tournament.exception.ResourceNotFoundException;
import com.cricket.tournament.repository.InningsRepository;
import com.cricket.tournament.repository.MatchRepository;
import com.cricket.tournament.repository.TeamRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.cricket.tournament.repository.BallDeliveryRepository;
@Service
public class InningsService {

    private final InningsRepository inningsRepository;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final BallDeliveryRepository deliveryRepository;

    public InningsService(
            InningsRepository inningsRepository,
            MatchRepository matchRepository,
            TeamRepository teamRepository,
            PlayerRepository playerRepository,
            BallDeliveryRepository deliveryRepository) {

        this.inningsRepository = inningsRepository;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @Transactional
    public InningsResponse startInnings(
            Long matchId,
            InningsRequest request) {

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Match not found with ID: " + matchId));

        // Do not start an innings for a completed or cancelled match.
        if ("COMPLETED".equalsIgnoreCase(match.getStatus())
                || "CANCELLED".equalsIgnoreCase(match.getStatus())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot start an innings for a completed or cancelled match");
        }

        int inningsNumber = request.getInningsNumber();

        if (inningsNumber < 1 || inningsNumber > 4) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Innings number must be between 1 and 4");
        }


        


        Team battingTeam = teamRepository.findById(
                request.getBattingTeamId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Batting team not found with ID: "
                                        + request.getBattingTeamId()));

        Team bowlingTeam = teamRepository.findById(
                request.getBowlingTeamId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bowling team not found with ID: "
                                        + request.getBowlingTeamId()));

        // Both teams must belong to the match's tournament.
        Long tournamentId = match.getTournament().getId();

        if (!tournamentId.equals(battingTeam.getTournament().getId())
                || !tournamentId.equals(
                        bowlingTeam.getTournament().getId())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Both teams must belong to the match's tournament");
        }

        // A team cannot bat against itself.
        if (battingTeam.getId().equals(bowlingTeam.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Batting and bowling teams must be different");
        }
        
     // Load the opening striker.
        Player striker = playerRepository.findById(request.getStrikerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Striker not found with ID: " + request.getStrikerId()));

        // Load the opening non-striker.
        Player nonStriker = playerRepository.findById(request.getNonStrikerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Non-striker not found with ID: " + request.getNonStrikerId()));

        // The same player cannot occupy both batting positions.
        if (striker.getId().equals(nonStriker.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Striker and non-striker must be different players");
        }

        // Both opening batters must belong to the batting team.
        if (!striker.getTeam().getId().equals(battingTeam.getId())
                || !nonStriker.getTeam().getId().equals(battingTeam.getId())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Both opening batters must belong to the batting team");
        }

        // For a two-team match, the innings teams must match the scheduled teams.
        Long team1Id = match.getTeam1().getId();
        Long team2Id = match.getTeam2().getId();

        boolean validPair =
                (battingTeam.getId().equals(team1Id)
                        && bowlingTeam.getId().equals(team2Id))
                || (battingTeam.getId().equals(team2Id)
                        && bowlingTeam.getId().equals(team1Id));

        if (!validPair) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The innings teams must be the two teams scheduled for this match");
        }

        // Validate the previous innings and the batting/bowling teams.
        if (inningsNumber > 1) {

            Innings previousInnings = inningsRepository
                    .findByMatchIdOrderByInningsNumberAsc(matchId)
                    .stream()
                    .filter(existing ->
                            existing.getInningsNumber() == inningsNumber - 1)
                    .findFirst()
                    .orElseThrow(() ->
                            new ResponseStatusException(
                                    HttpStatus.CONFLICT,
                           
                                   "Start the previous innings before starting innings "
                                            + inningsNumber));

            if (!battingTeam.getId().equals(
                    previousInnings.getBowlingTeam().getId())) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "The batting team must be the previous innings' bowling team");
            }

            if (!bowlingTeam.getId().equals(
                    previousInnings.getBattingTeam().getId())) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "The bowling team must be the previous innings' batting team");
            }

            if (!"COMPLETED".equalsIgnoreCase(
                    previousInnings.getStatus())) {

                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "The previous innings must be completed before starting innings "
                                + inningsNumber);
            }
        }



        // Keep this MVP sequential: only one unfinished innings per match.
        boolean unfinishedInningsExists =
                inningsRepository
                        .findByMatchIdOrderByInningsNumberAsc(matchId)
                        .stream()
                        .anyMatch(existing ->
                                "IN_PROGRESS".equalsIgnoreCase(
                                        existing.getStatus()));

        if (unfinishedInningsExists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Complete the current innings before starting another");
        }

        Innings innings = new Innings();

        innings.setMatch(match);
        innings.setInningsNumber(inningsNumber);
        innings.setBattingTeam(battingTeam);
        innings.setBowlingTeam(bowlingTeam);

        innings.setStriker(striker);
        innings.setNonStriker(nonStriker);

        innings.setTotalRuns(0);
        innings.setWickets(0);
        innings.setLegalBalls(0);
        innings.setStatus("IN_PROGRESS");

        // Test matches have no fixed innings overs limit in this MVP.
        if ("TEST".equalsIgnoreCase(match.getFormat())) {
            innings.setOversLimit(null);
        } else {
            Integer oversLimit = match.getCurrentOvers();

            if (oversLimit == null || oversLimit <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "A valid current overs limit is required to start this innings");
            }

            innings.setOversLimit(oversLimit);
        }

        Innings savedInnings = inningsRepository.save(innings);

        // Mark the match as live.
        match.setStatus("LIVE");
        matchRepository.save(match);

        return convertToResponse(savedInnings);
    }
    
    @Transactional(readOnly = true)
    public java.util.List<InningsResponse> getInningsByMatch(Long matchId) {

        if (!matchRepository.existsById(matchId)) {
            throw new ResourceNotFoundException(
                    "Match not found with ID: " + matchId);
        }

        return inningsRepository
                .findByMatchIdOrderByInningsNumberAsc(matchId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public InningsResponse getInningsById(Long inningsId) {

        Innings innings = inningsRepository.findById(inningsId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Innings not found with ID: " + inningsId));

        return convertToResponse(innings);
    }


    @Transactional
    public InningsResponse assignOpeningBatters(
            Long inningsId,
            InningsRequest request) {

        Innings innings = inningsRepository.findById(inningsId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Innings not found with ID: " + inningsId));

        if (!"IN_PROGRESS".equalsIgnoreCase(innings.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Opening batters can only be assigned to an innings in progress");
        }

        // Do not overwrite batters if deliveries have already been recorded.
        if (deliveryExistsForInnings(inningsId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Opening batters cannot be changed after deliveries have been recorded");
        }

        Player striker = playerRepository.findById(request.getStrikerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Striker not found with ID: " + request.getStrikerId()));

        Player nonStriker = playerRepository.findById(request.getNonStrikerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Non-striker not found with ID: " + request.getNonStrikerId()));

        if (striker.getId().equals(nonStriker.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Striker and non-striker must be different players");
        }

        Long battingTeamId = innings.getBattingTeam().getId();

        if (!striker.getTeam().getId().equals(battingTeamId)
                || !nonStriker.getTeam().getId().equals(battingTeamId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Both batters must belong to the innings' batting team");
        }

        innings.setStriker(striker);
        innings.setNonStriker(nonStriker);

        return convertToResponse(inningsRepository.save(innings));
    }

    private boolean deliveryExistsForInnings(Long inningsId) {
        return deliveryRepository.existsByInningsIdAndDeliveryNumberGreaterThan(
                inningsId, 0);
    }
 

    
    private InningsResponse convertToResponse(Innings innings) {

        InningsResponse response = new InningsResponse();

        // Existing innings details
        response.setId(innings.getId());
        response.setMatchId(innings.getMatch().getId());
        response.setMatchName(innings.getMatch().getMatchName());

        response.setInningsNumber(innings.getInningsNumber());

        response.setBattingTeamId(innings.getBattingTeam().getId());
        response.setBattingTeamName(innings.getBattingTeam().getName());

        response.setBowlingTeamId(innings.getBowlingTeam().getId());
        response.setBowlingTeamName(innings.getBowlingTeam().getName());

        if (innings.getStriker() != null) {
            response.setStrikerId(innings.getStriker().getId());
            response.setStrikerName(innings.getStriker().getName());
        }

        if (innings.getNonStriker() != null) {
            response.setNonStrikerId(innings.getNonStriker().getId());
            response.setNonStrikerName(innings.getNonStriker().getName());
        }

        response.setTotalRuns(innings.getTotalRuns());
        response.setWickets(innings.getWickets());
        response.setLegalBalls(innings.getLegalBalls());
        response.setOversLimit(innings.getOversLimit());
        response.setStatus(innings.getStatus());

        // Display completed overs in cricket notation
        int legalBalls = innings.getLegalBalls();

        int completedOvers = legalBalls / 6;
        int ballsInCurrentOver = legalBalls % 6;

        response.setOversCompleted(
                completedOvers + "." + ballsInCurrentOver
        );

        // Calculate chase statistics only for the second innings
        if (innings.getInningsNumber() == 2) {

            Match match = innings.getMatch();

            Innings firstInnings = inningsRepository
                    .findByMatchIdOrderByInningsNumberAsc(match.getId())
                    .stream()
                    .filter(i -> i.getInningsNumber() == 1)
                    .findFirst()
                    .orElse(null);

            if (firstInnings != null) {

                int target = firstInnings.getTotalRuns() + 1;

                int runsLeft = Math.max(
                        0,
                        target - innings.getTotalRuns()
                );

                int totalBalls = innings.getOversLimit() * 6;

                int ballsRemaining = Math.max(
                        0,
                        totalBalls - innings.getLegalBalls()
                );

                int remainingOvers = ballsRemaining / 6;
                int remainingBalls = ballsRemaining % 6;

                response.setTarget(target);
                response.setRunsLeft(runsLeft);
                response.setBallsRemaining(ballsRemaining);

                response.setOversRemaining(
                        remainingOvers + "." + remainingBalls
                );

                // Required run rate
                if (ballsRemaining > 0 && runsLeft > 0) {

                    double requiredRunRate =
                            (runsLeft * 6.0) / ballsRemaining;

                    response.setRequiredRunRate(
                            Math.round(requiredRunRate * 100.0) / 100.0
                    );

                } else {
                    response.setRequiredRunRate(0.0);
                }
            }
        }

        return response;
    }
}