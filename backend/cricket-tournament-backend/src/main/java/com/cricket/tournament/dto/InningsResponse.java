package com.cricket.tournament.dto;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InningsResponse {

    private Long id;
    private Long matchId;
    private String matchName;

    private int inningsNumber;

    private Long battingTeamId;
    private String battingTeamName;

    private Long bowlingTeamId;
    private String bowlingTeamName;

 // Opening batters
    private Long strikerId;
    private String strikerName;

    private Long nonStrikerId;
    private String nonStrikerName;
    
    private int totalRuns;
    private int wickets;
    private int legalBalls;

    private Integer oversLimit;
    private String status;
    
    // Display overs in cricket notation: 0.1, 1.4, 2.0
    private String oversCompleted;

    // Second-innings chase statistics
    private Integer target;
    private Integer runsLeft;
    private Integer ballsRemaining;
    private String oversRemaining;
    private Double requiredRunRate;
    
 // Add getters and setters for the new fields

    public String getOversCompleted() {
        return oversCompleted;
    }

    public void setOversCompleted(String oversCompleted) {
        this.oversCompleted = oversCompleted;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public Integer getRunsLeft() {
        return runsLeft;
    }

    public void setRunsLeft(Integer runsLeft) {
        this.runsLeft = runsLeft;
    }

    public Integer getBallsRemaining() {
        return ballsRemaining;
    }

    public void setBallsRemaining(Integer ballsRemaining) {
        this.ballsRemaining = ballsRemaining;
    }

    public String getOversRemaining() {
        return oversRemaining;
    }

    public void setOversRemaining(String oversRemaining) {
        this.oversRemaining = oversRemaining;
    }

    public Double getRequiredRunRate() {
        return requiredRunRate;
    }

    public void setRequiredRunRate(Double requiredRunRate) {
        this.requiredRunRate = requiredRunRate;
    }


    public InningsResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public int getInningsNumber() {
        return inningsNumber;
    }

    public void setInningsNumber(int inningsNumber) {
        this.inningsNumber = inningsNumber;
    }

    public Long getBattingTeamId() {
        return battingTeamId;
    }

    public void setBattingTeamId(Long battingTeamId) {
        this.battingTeamId = battingTeamId;
    }

    public String getBattingTeamName() {
        return battingTeamName;
    }

    public void setBattingTeamName(String battingTeamName) {
        this.battingTeamName = battingTeamName;
    }

    public Long getBowlingTeamId() {
        return bowlingTeamId;
    }

    public void setBowlingTeamId(Long bowlingTeamId) {
        this.bowlingTeamId = bowlingTeamId;
    }

    public String getBowlingTeamName() {
        return bowlingTeamName;
    }

    public void setBowlingTeamName(String bowlingTeamName) {
        this.bowlingTeamName = bowlingTeamName;
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(int totalRuns) {
        this.totalRuns = totalRuns;
    }

    public int getWickets() {
        return wickets;
    }

    public void setWickets(int wickets) {
        this.wickets = wickets;
    }

    public int getLegalBalls() {
        return legalBalls;
    }

    public void setLegalBalls(int legalBalls) {
        this.legalBalls = legalBalls;
    }

    public Integer getOversLimit() {
        return oversLimit;
    }

    public void setOversLimit(Integer oversLimit) {
        this.oversLimit = oversLimit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Long getStrikerId() {
        return strikerId;
    }

    public void setStrikerId(Long strikerId) {
        this.strikerId = strikerId;
    }

    public String getStrikerName() {
        return strikerName;
    }

    public void setStrikerName(String strikerName) {
        this.strikerName = strikerName;
    }

    public Long getNonStrikerId() {
        return nonStrikerId;
    }

    public void setNonStrikerId(Long nonStrikerId) {
        this.nonStrikerId = nonStrikerId;
    }

    public String getNonStrikerName() {
        return nonStrikerName;
    }

    public void setNonStrikerName(String nonStrikerName) {
        this.nonStrikerName = nonStrikerName;
    }
}