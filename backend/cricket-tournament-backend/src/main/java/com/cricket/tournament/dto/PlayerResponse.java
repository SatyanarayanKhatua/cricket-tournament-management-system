package com.cricket.tournament.dto;

public class PlayerResponse {

    private Long id;
    private String name;
    private String role;
    private String battingStyle;
    private String bowlingStyle;
    private Long teamId;
    private String teamName;

    public PlayerResponse() {
    }

    public PlayerResponse(
            Long id,
            String name,
            String role,
            String battingStyle,
            String bowlingStyle,
            Long teamId,
            String teamName) {

        this.id = id;
        this.name = name;
        this.role = role;
        this.battingStyle = battingStyle;
        this.bowlingStyle = bowlingStyle;
        this.teamId = teamId;
        this.teamName = teamName;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getBattingStyle() {
        return battingStyle;
    }

    public String getBowlingStyle() {
        return bowlingStyle;
    }

    public Long getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }
}