package com.cricket.tournament.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class MatchRequest {

    @NotBlank(message = "Match name is required")
    @Size(max = 150, message = "Match name cannot exceed 150 characters")
    private String matchName;

    @NotBlank(message = "Match format is required")
    private String format;

    @NotBlank(message = "Venue is required")
    private String venue;

    @NotNull(message = "Match date and time are required")
    private LocalDateTime matchDateTime;

    @NotNull(message = "Team 1 ID is required")
    @Positive(message = "Team 1 ID must be positive")
    private Long team1Id;

    @NotNull(message = "Team 2 ID is required")
    @Positive(message = "Team 2 ID must be positive")
    private Long team2Id;

    @Positive(message = "Overs must be greater than zero")
    private Integer scheduledOvers;

    public MatchRequest() {
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public LocalDateTime getMatchDateTime() {
        return matchDateTime;
    }

    public void setMatchDateTime(LocalDateTime matchDateTime) {
        this.matchDateTime = matchDateTime;
    }

    public Long getTeam1Id() {
        return team1Id;
    }

    public void setTeam1Id(Long team1Id) {
        this.team1Id = team1Id;
    }

    public Long getTeam2Id() {
        return team2Id;
    }

    public void setTeam2Id(Long team2Id) {
        this.team2Id = team2Id;
    }

    public Integer getScheduledOvers() {
        return scheduledOvers;
    }

    public void setScheduledOvers(Integer scheduledOvers) {
        this.scheduledOvers = scheduledOvers;
    }
}