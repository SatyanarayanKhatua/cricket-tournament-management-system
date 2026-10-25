package com.cricket.tournament.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OversRevisionRequest {

    @NotNull(message = "Revised overs are required")
    @Positive(message = "Revised overs must be greater than zero")
    private Integer revisedOvers;

    @NotBlank(message = "Revision reason is required")
    private String reason;

    public OversRevisionRequest() {
    }

    public Integer getRevisedOvers() {
        return revisedOvers;
    }

    public void setRevisedOvers(Integer revisedOvers) {
        this.revisedOvers = revisedOvers;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}