package com.cricket.tournament.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BallDeliveryRequest {

    @NotNull(message = "Striker ID is required")
    @Positive(message = "Striker ID must be positive")
    private Long strikerId;

    @NotNull(message = "Non-striker ID is required")
    @Positive(message = "Non-striker ID must be positive")
    private Long nonStrikerId;

    @NotNull(message = "Bowler ID is required")
    @Positive(message = "Bowler ID must be positive")
    private Long bowlerId;

    @Min(value = 0, message = "Bat runs cannot be negative")
    @Max(value = 6, message = "Bat runs must be between 0 and 6")
    private int batRuns = 0;

    @Min(value = 0, message = "Wide runs cannot be negative")
    private int wideRuns = 0;

    @Min(value = 0, message = "No-ball runs cannot be negative")
    private int noBallRuns = 0;

    @Min(value = 0, message = "Bye runs cannot be negative")
    private int byeRuns = 0;

    @Min(value = 0, message = "Leg-bye runs cannot be negative")
    private int legByeRuns = 0;

    @Min(value = 0, message = "Penalty runs cannot be negative")
    private int penaltyRuns = 0;

    private boolean wicket = false;

    private String dismissalType;

    @Positive(message = "Dismissed player ID must be positive")
    private Long dismissedPlayerId;

    @Positive(message = "Fielder ID must be positive")
    private Long fielderId;
    
    @Positive(message = "Incoming batter ID must be positive")
    private Long incomingBatterId;
    
    private Boolean battersCrossed;
    
    private String runOutEnd;


    private String commentary;

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

    public Long getBowlerId() {
        return bowlerId;
    }

    public void setBowlerId(Long bowlerId) {
        this.bowlerId = bowlerId;
    }

    public int getBatRuns() {
        return batRuns;
    }

    public void setBatRuns(int batRuns) {
        this.batRuns = batRuns;
    }

    public int getWideRuns() {
        return wideRuns;
    }

    public void setWideRuns(int wideRuns) {
        this.wideRuns = wideRuns;
    }

    public int getNoBallRuns() {
        return noBallRuns;
    }

    public void setNoBallRuns(int noBallRuns) {
        this.noBallRuns = noBallRuns;
    }

    public int getByeRuns() {
        return byeRuns;
    }

    public void setByeRuns(int byeRuns) {
        this.byeRuns = byeRuns;
    }

    public int getLegByeRuns() {
        return legByeRuns;
    }

    public void setLegByeRuns(int legByeRuns) {
        this.legByeRuns = legByeRuns;
    }

    public int getPenaltyRuns() {
        return penaltyRuns;
    }

    public void setPenaltyRuns(int penaltyRuns) {
        this.penaltyRuns = penaltyRuns;
    }

    public boolean isWicket() {
        return wicket;
    }

    public void setWicket(boolean wicket) {
        this.wicket = wicket;
    }

    public String getDismissalType() {
        return dismissalType;
    }

    public void setDismissalType(String dismissalType) {
        this.dismissalType = dismissalType;
    }

    public Long getDismissedPlayerId() {
        return dismissedPlayerId;
    }

    public void setDismissedPlayerId(Long dismissedPlayerId) {
        this.dismissedPlayerId = dismissedPlayerId;
    }

    public Long getFielderId() {
        return fielderId;
    }

    public void setFielderId(Long fielderId) {
        this.fielderId = fielderId;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }
    public Long getIncomingBatterId() {
        return incomingBatterId;
    }

    public void setIncomingBatterId(Long incomingBatterId) {
        this.incomingBatterId = incomingBatterId;
    }
    public Boolean getBattersCrossed() {
        return battersCrossed;
    }

    public void setBattersCrossed(Boolean battersCrossed) {
        this.battersCrossed = battersCrossed;
    }
    public String getRunOutEnd() {
        return runOutEnd;
    }

    public void setRunOutEnd(String runOutEnd) {
        this.runOutEnd = runOutEnd;
    }
}