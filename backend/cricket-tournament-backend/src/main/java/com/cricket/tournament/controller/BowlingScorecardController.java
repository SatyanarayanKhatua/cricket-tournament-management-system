package com.cricket.tournament.controller;

import com.cricket.tournament.dto.BowlerFiguresResponse;
import com.cricket.tournament.service.BowlingScorecardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/innings/{inningsId}/scorecard")
public class BowlingScorecardController {


private final BowlingScorecardService bowlingScorecardService;

public BowlingScorecardController(
        BowlingScorecardService bowlingScorecardService) {
    this.bowlingScorecardService = bowlingScorecardService;
}

@GetMapping("/bowling")
public List<BowlerFiguresResponse> getBowlingScorecard(
        @PathVariable Long inningsId) {

    return bowlingScorecardService.getBowlingScorecard(inningsId);
}


}
