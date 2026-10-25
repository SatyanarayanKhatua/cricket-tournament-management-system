package com.cricket.tournament.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "innings",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_match_innings_number",
            columnNames = {"match_id", "innings_number"}
        )
    }
)
public class Innings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Column(name = "innings_number", nullable = false)
    private int inningsNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "batting_team_id", nullable = false)
    private Team battingTeam;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bowling_team_id", nullable = false)
    private Team bowlingTeam;

    @Column(nullable = false)
    private int totalRuns = 0;

    @Column(nullable = false)
    private int wickets = 0;

    // Store legal deliveries as an integer.
    // Example: 10 overs and 3 balls = 63 legal balls.
    @Column(nullable = false)
    private int legalBalls = 0;

    // Null means there is no fixed overs limit, as in Test cricket.
    private Integer oversLimit;

    @Column(nullable = false)
    private String status = "NOT_STARTED";
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "striker_id")
    private Player striker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "non_striker_id")
    private Player nonStriker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_bowler_id")
    private Player currentBowler;

    @Column(name = "next_delivery_number", nullable = false)
    private int nextDeliveryNumber = 1;
    
    @Column(name = "bowler_selection_required", nullable = false)
    private boolean bowlerSelectionRequired = false;

    public Innings() {
    }

    public Long getId() {
        return id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public int getInningsNumber() {
        return inningsNumber;
    }

    public void setInningsNumber(int inningsNumber) {
        this.inningsNumber = inningsNumber;
    }

    public Team getBattingTeam() {
        return battingTeam;
    }

    public void setBattingTeam(Team battingTeam) {
        this.battingTeam = battingTeam;
    }

    public Team getBowlingTeam() {
        return bowlingTeam;
    }

    public void setBowlingTeam(Team bowlingTeam) {
        this.bowlingTeam = bowlingTeam;
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
    
    public Player getStriker() {
        return striker;
    }

    public void setStriker(Player striker) {
        this.striker = striker;
    }

    public Player getNonStriker() {
        return nonStriker;
    }

    public void setNonStriker(Player nonStriker) {
        this.nonStriker = nonStriker;
    }

    public Player getCurrentBowler() {
        return currentBowler;
    }

    public void setCurrentBowler(Player currentBowler) {
        this.currentBowler = currentBowler;
    }

    public int getNextDeliveryNumber() {
        return nextDeliveryNumber;
    }

    public void setNextDeliveryNumber(int nextDeliveryNumber) {
        this.nextDeliveryNumber = nextDeliveryNumber;
    }
    
    public boolean isBowlerSelectionRequired() {
        return bowlerSelectionRequired;
    }

    public void setBowlerSelectionRequired(boolean bowlerSelectionRequired) {
        this.bowlerSelectionRequired = bowlerSelectionRequired;
    }
}