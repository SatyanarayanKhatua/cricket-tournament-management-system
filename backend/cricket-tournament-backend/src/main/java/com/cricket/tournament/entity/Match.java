package com.cricket.tournament.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Match name is required")
    @Size(max = 150, message = "Match name cannot exceed 150 characters")
    private String matchName;

    @NotBlank(message = "Match format is required")
    private String format;

    @NotBlank(message = "Venue is required")
    private String venue;

    @NotNull(message = "Match date and time are required")
    private LocalDateTime matchDateTime;

    @NotBlank(message = "Match status is required")
    private String status = "SCHEDULED";

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team1_id", nullable = false)
    private Team team1;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team2_id", nullable = false)
    private Team team2;
    
    private Integer scheduledOvers;

    private Integer currentOvers;

    private String oversRevisionReason;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_team_id")
    private Team winner;

    @Column(name = "result_type", length = 50)
    private String resultType;

    @Column(name = "result_summary", length = 255)
    private String resultSummary;

    public Match() {
    }

    public Long getId() {
        return id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }
    
    public Integer getScheduledOvers() {
        return scheduledOvers;
    }

    public void setScheduledOvers(Integer scheduledOvers) {
        this.scheduledOvers = scheduledOvers;
    }

    public Integer getCurrentOvers() {
        return currentOvers;
    }

    public void setCurrentOvers(Integer currentOvers) {
        this.currentOvers = currentOvers;
    }

    public String getOversRevisionReason() {
        return oversRevisionReason;
    }

    public void setOversRevisionReason(String oversRevisionReason) {
        this.oversRevisionReason = oversRevisionReason;
    }
    
    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getResultSummary() {
        return resultSummary;
    }

    public void setResultSummary(String resultSummary) {
        this.resultSummary = resultSummary;
    }
}