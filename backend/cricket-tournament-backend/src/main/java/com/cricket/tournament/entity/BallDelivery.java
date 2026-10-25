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
    name = "ball_deliveries",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_innings_delivery_number",
            columnNames = {"innings_id", "delivery_number"}
        )
    }
)
public class BallDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "innings_id", nullable = false)
    private Innings innings;

    // Sequential record number within an innings.
    // This is not the same as the legal ball number.
    @Column(name = "delivery_number", nullable = false)
    private int deliveryNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "striker_id", nullable = false)
    private Player striker;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "non_striker_id", nullable = false)
    private Player nonStriker;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bowler_id", nullable = false)
    private Player bowler;

    @Column(name = "bat_runs", nullable = false)
    private int batRuns = 0;

    @Column(name = "wide_runs", nullable = false)
    private int wideRuns = 0;

    @Column(name = "no_ball_runs", nullable = false)
    private int noBallRuns = 0;

    @Column(name = "bye_runs", nullable = false)
    private int byeRuns = 0;

    @Column(name = "leg_bye_runs", nullable = false)
    private int legByeRuns = 0;

    @Column(name = "penalty_runs", nullable = false)
    private int penaltyRuns = 0;

    // Total runs added to the team's innings on this delivery.
    @Column(name = "total_runs", nullable = false)
    private int totalRuns = 0;

    // False for wides and no-balls in the standard scoring model.
    @Column(name = "legal_delivery", nullable = false)
    private boolean legalDelivery = true;

    @Column(nullable = false)
    private boolean wicket = false;

    // Examples: BOWLED, CAUGHT, LBW, RUN_OUT, STUMPED.
    private String dismissalType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dismissed_player_id")
    private Player dismissedPlayer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fielder_id")
    private Player fielder;
    
 // Runs charged to the bowler for this delivery.
    @Column(name = "bowler_runs_conceded", nullable = false)
    private int bowlerRunsConceded = 0;

    // Whether this dismissal counts as a wicket credited to the bowler.
    @Column(name = "bowler_wicket", nullable = false)
    private boolean bowlerWicket = false;

    // Batting state before this delivery.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "striker_before_id")
    private Player strikerBefore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "non_striker_before_id")
    private Player nonStrikerBefore;

    // Bowler selected before this delivery.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bowler_before_id")
    private Player bowlerBefore;

    // Number of legal balls before this delivery.
    @Column(name = "legal_balls_before", nullable = false)
    private int legalBallsBefore;
    
    @Column(name = "batters_crossed")
    private Boolean battersCrossed;
    
    @Column(name = "run_out_end", length = 20)
    private String runOutEnd;

    @Column(length = 500)
    private String commentary;

    public BallDelivery() {
    }

    public Long getId() {
        return id;
    }

    public Innings getInnings() {
        return innings;
    }

    public void setInnings(Innings innings) {
        this.innings = innings;
    }

    public int getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setDeliveryNumber(int deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
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

    public Player getBowler() {
        return bowler;
    }

    public void setBowler(Player bowler) {
        this.bowler = bowler;
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

    public int getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(int totalRuns) {
        this.totalRuns = totalRuns;
    }

    public boolean isLegalDelivery() {
        return legalDelivery;
    }

    public void setLegalDelivery(boolean legalDelivery) {
        this.legalDelivery = legalDelivery;
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

    public Player getDismissedPlayer() {
        return dismissedPlayer;
    }

    public void setDismissedPlayer(Player dismissedPlayer) {
        this.dismissedPlayer = dismissedPlayer;
    }

    public Player getFielder() {
        return fielder;
    }

    public void setFielder(Player fielder) {
        this.fielder = fielder;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }
    
    public int getBowlerRunsConceded() {
        return bowlerRunsConceded;
    }

    public void setBowlerRunsConceded(int bowlerRunsConceded) {
        this.bowlerRunsConceded = bowlerRunsConceded;
    }

    public boolean isBowlerWicket() {
        return bowlerWicket;
    }

    public void setBowlerWicket(boolean bowlerWicket) {
        this.bowlerWicket = bowlerWicket;
    }

    public Player getStrikerBefore() {
        return strikerBefore;
    }

    public void setStrikerBefore(Player strikerBefore) {
        this.strikerBefore = strikerBefore;
    }

    public Player getNonStrikerBefore() {
        return nonStrikerBefore;
    }

    public void setNonStrikerBefore(Player nonStrikerBefore) {
        this.nonStrikerBefore = nonStrikerBefore;
    }

    public Player getBowlerBefore() {
        return bowlerBefore;
    }

    public void setBowlerBefore(Player bowlerBefore) {
        this.bowlerBefore = bowlerBefore;
    }

    public int getLegalBallsBefore() {
        return legalBallsBefore;
    }

    public void setLegalBallsBefore(int legalBallsBefore) {
        this.legalBallsBefore = legalBallsBefore;
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