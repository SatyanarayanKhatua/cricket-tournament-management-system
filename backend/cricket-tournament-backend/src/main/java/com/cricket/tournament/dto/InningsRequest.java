package com.cricket.tournament.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class InningsRequest {

    @NotNull(message = "Innings number is required")
    @Positive(message = "Innings number must be positive")
    private Integer inningsNumber;

    @NotNull(message = "Batting team ID is required")
    @Positive(message = "Batting team ID must be positive")
    private Long battingTeamId;

    @NotNull(message = "Bowling team ID is required")
    @Positive(message = "Bowling team ID must be positive")
    private Long bowlingTeamId;
    
    @NotNull(message = "Opening striker ID is required")
    @Positive(message = "Opening striker ID must be positive")
    private Long strikerId;

    @NotNull(message = "Opening non-striker ID is required")
    @Positive(message = "Opening non-striker ID must be positive")
    private Long nonStrikerId;

    public Integer getInningsNumber() {
        return inningsNumber;
    }

    public void setInningsNumber(Integer inningsNumber) {
        this.inningsNumber = inningsNumber;
    }

    public Long getBattingTeamId() {
        return battingTeamId;
    }

    public void setBattingTeamId(Long battingTeamId) {
        this.battingTeamId = battingTeamId;
    }

    public Long getBowlingTeamId() {
        return bowlingTeamId;
    }

    public void setBowlingTeamId(Long bowlingTeamId) {
        this.bowlingTeamId = bowlingTeamId;
    }
    
    public Long getStrikerId() {
        return strikerId;
    }

    public void setStrikerId(Long strikerId) {
        this.strikerId = strikerId;
    }

    public Long getNonStrikerId() {
        return nonStrikerId;
    }

    public void setNonStrikerId(Long nonStrikerId) {
        this.nonStrikerId = nonStrikerId;
    }
}