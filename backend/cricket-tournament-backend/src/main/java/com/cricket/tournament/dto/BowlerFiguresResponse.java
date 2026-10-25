package com.cricket.tournament.dto;

public class BowlerFiguresResponse {

    private Long bowlerId;
    private String bowlerName;

    // Store legal balls as an integer internally.
    private int legalBalls;

    private int runsConceded;
    private int wickets;
    private double economy;

    public BowlerFiguresResponse() {
    }

    public Long getBowlerId() {
        return bowlerId;
    }

    public void setBowlerId(Long bowlerId) {
        this.bowlerId = bowlerId;
    }

    public String getBowlerName() {
        return bowlerName;
    }

    public void setBowlerName(String bowlerName) {
        this.bowlerName = bowlerName;
    }

    public int getLegalBalls() {
        return legalBalls;
    }

    public void setLegalBalls(int legalBalls) {
        this.legalBalls = legalBalls;
    }

    public int getRunsConceded() {
        return runsConceded;
    }

    public void setRunsConceded(int runsConceded) {
        this.runsConceded = runsConceded;
    }

    public int getWickets() {
        return wickets;
    }

    public void setWickets(int wickets) {
        this.wickets = wickets;
    }

    public double getEconomy() {
        return economy;
    }

    public void setEconomy(double economy) {
        this.economy = economy;
    }

    // Display 10 legal balls as 1.4 overs, not 1.67 overs.
    public String getOvers() {
        return (legalBalls / 6) + "." + (legalBalls % 6);
    }
}