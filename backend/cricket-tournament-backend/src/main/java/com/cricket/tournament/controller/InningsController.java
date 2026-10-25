package com.cricket.tournament.controller;

import com.cricket.tournament.dto.InningsRequest;
import com.cricket.tournament.dto.InningsResponse;
import com.cricket.tournament.service.InningsService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches/{matchId}/innings")
public class InningsController {

    private final InningsService inningsService;

    public InningsController(InningsService inningsService) {
        this.inningsService = inningsService;
    }

    // Start a new innings.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InningsResponse startInnings(
            @PathVariable Long matchId,
            @Valid @RequestBody InningsRequest request) {

        return inningsService.startInnings(matchId, request);
    }
    
 // Get all innings for a match.
    @GetMapping
    public java.util.List<InningsResponse> getInningsByMatch(
            @PathVariable Long matchId) {

        return inningsService.getInningsByMatch(matchId);
    }

    // Get a specific innings by ID.
    @GetMapping("/{inningsId}")
    public InningsResponse getInningsById(
            @PathVariable Long matchId,
            @PathVariable Long inningsId) {

        InningsResponse response =
                inningsService.getInningsById(inningsId);

        if (!response.getMatchId().equals(matchId)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND,
                    "Innings does not belong to the specified match");
        }

        return response;
    }
    
 // Assign opening batters to an existing innings.
 @PatchMapping("/{inningsId}/openers")
 public InningsResponse assignOpeningBatters(
         @PathVariable Long matchId,
         @PathVariable Long inningsId,
         @Valid @RequestBody InningsRequest request) {

     InningsResponse response =
             inningsService.assignOpeningBatters(inningsId, request);

     if (!response.getMatchId().equals(matchId)) {
         throw new org.springframework.web.server.ResponseStatusException(
                 org.springframework.http.HttpStatus.NOT_FOUND,
                 "Innings does not belong to the specified match");
     }

     return response;
 }
 

}