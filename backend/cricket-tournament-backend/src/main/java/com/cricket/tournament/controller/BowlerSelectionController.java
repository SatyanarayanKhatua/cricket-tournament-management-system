package com.cricket.tournament.controller;

import com.cricket.tournament.dto.EligibleBowlerResponse;
import com.cricket.tournament.dto.SelectBowlerRequest;
import com.cricket.tournament.service.BowlerSelectionService;

import jakarta.validation.Valid;


import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/innings/{inningsId}")
public class BowlerSelectionController {

    private final BowlerSelectionService bowlerSelectionService;

    public BowlerSelectionController(
            BowlerSelectionService bowlerSelectionService) {
        this.bowlerSelectionService = bowlerSelectionService;
    }

    @GetMapping("/eligible-bowlers")
    public List<EligibleBowlerResponse> getEligibleBowlers(
            @PathVariable Long inningsId) {

        return bowlerSelectionService.getEligibleBowlers(inningsId);
    }

    @PutMapping("/bowler")
    public Map<String, String> selectBowler(
            @PathVariable Long inningsId,
            @Valid @RequestBody SelectBowlerRequest request) {

        bowlerSelectionService.selectBowler(
                inningsId,
                request.getBowlerId());

        return Map.of(
                "message", "Next bowler selected successfully");
    }
}