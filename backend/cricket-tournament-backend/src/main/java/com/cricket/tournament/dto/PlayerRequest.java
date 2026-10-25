package com.cricket.tournament.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PlayerRequest {

    @NotBlank(message = "Player name is required")
    @Size(max = 100, message = "Player name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Player role is required")
    private String role;

    @NotBlank(message = "Batting style is required")
    private String battingStyle;

    private String bowlingStyle;

    public PlayerRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBattingStyle() {
        return battingStyle;
    }

    public void setBattingStyle(String battingStyle) {
        this.battingStyle = battingStyle;
    }

    public String getBowlingStyle() {
        return bowlingStyle;
    }

    public void setBowlingStyle(String bowlingStyle) {
        this.bowlingStyle = bowlingStyle;
    }
}