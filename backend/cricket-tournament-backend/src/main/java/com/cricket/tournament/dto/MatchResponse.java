package com.cricket.tournament.dto;

import java.time.LocalDateTime;

public class MatchResponse {

    private Long id;
    private String matchName;
    private String format;
    private String venue;
    private LocalDateTime matchDateTime;
    private String status;

    private Long tournamentId;
    private String tournamentName;

    private Long team1Id;
    private String team1Name;

    private Long team2Id;
    private String team2Name;

    private Integer scheduledOvers;
    private Integer currentOvers;
    private String oversRevisionReason;
    
    private Long winnerTeamId;
    private String winnerTeamName;
    private String resultType;
    private String resultSummary;

    public MatchResponse() {
    }

    public MatchResponse(
            Long id,
            String matchName,
            String format,
            String venue,
            LocalDateTime matchDateTime,
            String status,
            Long tournamentId,
            String tournamentName,
            Long team1Id,
            String team1Name,
            Long team2Id,
            String team2Name,
            Integer scheduledOvers,
            Integer currentOvers,
            String oversRevisionReason,
            Long winnerTeamId,
            String winnerTeamName,
            String resultType,
            String resultSummary) {

        this.id = id;
        this.matchName = matchName;
        this.format = format;
        this.venue = venue;
        this.matchDateTime = matchDateTime;
        this.status = status;
        this.tournamentId = tournamentId;
        this.tournamentName = tournamentName;
        this.team1Id = team1Id;
        this.team1Name = team1Name;
        this.team2Id = team2Id;
        this.team2Name = team2Name;
        this.scheduledOvers = scheduledOvers;
        this.currentOvers = currentOvers;
        this.oversRevisionReason = oversRevisionReason;
        this.winnerTeamId = winnerTeamId;
        this.winnerTeamName = winnerTeamName;
        this.resultType = resultType;
        this.resultSummary = resultSummary;
    }

    public Long getId() {
        return id;
    }

    public String getMatchName() {
        return matchName;
    }

    public String getFormat() {
        return format;
    }

    public String getVenue() {
        return venue;
    }

    public LocalDateTime getMatchDateTime() {
        return matchDateTime;
    }

    public String getStatus() {
        return status;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public Long getTeam1Id() {
        return team1Id;
    }

    public String getTeam1Name() {
        return team1Name;
    }

    public Long getTeam2Id() {
        return team2Id;
    }

    public String getTeam2Name() {
        return team2Name;
    }

    public Integer getScheduledOvers() {
        return scheduledOvers;
    }

    public Integer getCurrentOvers() {
        return currentOvers;
    }

    public String getOversRevisionReason() {
        return oversRevisionReason;
    }
    public Long getWinnerTeamId() {
        return winnerTeamId;
    }

    public String getWinnerTeamName() {
        return winnerTeamName;
    }

    public String getResultType() {
        return resultType;
    }

    public String getResultSummary() {
        return resultSummary;
    }
}