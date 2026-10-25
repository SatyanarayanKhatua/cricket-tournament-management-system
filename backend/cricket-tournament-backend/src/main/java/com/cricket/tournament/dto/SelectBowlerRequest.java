package com.cricket.tournament.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class SelectBowlerRequest {

    @NotNull(message = "Bowler ID is required")
    @Positive(message = "Bowler ID must be positive")
    private Long bowlerId;

    public SelectBowlerRequest() {
    }

    public Long getBowlerId() {
        return bowlerId;
    }

    public void setBowlerId(Long bowlerId) {
        this.bowlerId = bowlerId;
    }
}