package com.cricket.tournament.dto;

public class EligibleBowlerResponse {

    private Long playerId;
    private String playerName;
    private String role;
    private String bowlingStyle;

    public EligibleBowlerResponse() {
    }

    public EligibleBowlerResponse(
            Long playerId,
            String playerName,
            String role,
            String bowlingStyle) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.role = role;
        this.bowlingStyle = bowlingStyle;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBowlingStyle() {
        return bowlingStyle;
    }

    public void setBowlingStyle(String bowlingStyle) {
        this.bowlingStyle = bowlingStyle;
    }
}