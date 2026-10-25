package com.cricket.tournament.controller;

import com.cricket.tournament.dto.BatterFiguresResponse;
import com.cricket.tournament.service.BatterFiguresService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/innings/{inningsId}/scorecard")
public class BattingScorecardController {


private final BatterFiguresService batterFiguresService;

public BattingScorecardController(
        BatterFiguresService batterFiguresService) {

    this.batterFiguresService = batterFiguresService;
}

@GetMapping("/batting")
public List<BatterFiguresResponse> getBattingScorecard(
        @PathVariable Long inningsId) {

    return batterFiguresService.getBattingScorecard(inningsId);
}


}
