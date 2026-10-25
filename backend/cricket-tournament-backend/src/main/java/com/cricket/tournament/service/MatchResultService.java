package com.cricket.tournament.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.cricket.tournament.entity.Innings;
import com.cricket.tournament.entity.Match;
import com.cricket.tournament.repository.InningsRepository;
import com.cricket.tournament.repository.MatchRepository;

@Service
public class MatchResultService {

    private final MatchRepository matchRepository;
    private final InningsRepository inningsRepository;

    public MatchResultService(
            MatchRepository matchRepository,
            InningsRepository inningsRepository) {

        this.matchRepository = matchRepository;
        this.inningsRepository = inningsRepository;
    }

    @Transactional
    public Match calculateAndSaveResult(Long matchId) {

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Match not found with ID: " + matchId));

        if ("COMPLETED".equalsIgnoreCase(match.getStatus())) {
            return match;
        }

        if ("TEST".equalsIgnoreCase(match.getFormat())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_IMPLEMENTED,
                    "Test match result calculation is not implemented yet");
        }

        List<Innings> inningsList =
                inningsRepository.findByMatchIdOrderByInningsNumberAsc(
                        matchId);

        Innings firstInnings = inningsList.stream()
                .filter(i -> i.getInningsNumber() == 1)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "First innings not found"));

        Innings secondInnings = inningsList.stream()
                .filter(i -> i.getInningsNumber() == 2)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Second innings not found"));

        if (!"COMPLETED".equalsIgnoreCase(firstInnings.getStatus())
                || !"COMPLETED".equalsIgnoreCase(secondInnings.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Both innings must be completed before calculating the result");
        }

        int firstScore = firstInnings.getTotalRuns();
        int secondScore = secondInnings.getTotalRuns();

        if (secondScore > firstScore) {

            int wicketsRemaining = 10 - secondInnings.getWickets();

            match.setWinner(secondInnings.getBattingTeam());
            match.setResultType("WON_BY_WICKETS");
            match.setResultSummary(
                    secondInnings.getBattingTeam().getName()
                    + " won by " + wicketsRemaining + " wickets");

        } else if (firstScore > secondScore) {

            int runDifference = firstScore - secondScore;

            match.setWinner(firstInnings.getBattingTeam());
            match.setResultType("WON_BY_RUNS");
            match.setResultSummary(
                    firstInnings.getBattingTeam().getName()
                    + " won by " + runDifference + " runs");

        } else {

            match.setWinner(null);
            match.setResultType("TIE");
            match.setResultSummary("Match tied");
        }

        match.setStatus("COMPLETED");

        return matchRepository.save(match);
    }
}

