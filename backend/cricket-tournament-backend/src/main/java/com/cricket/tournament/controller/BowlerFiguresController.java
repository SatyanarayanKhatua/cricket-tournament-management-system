package com.cricket.tournament.controller;

import com.cricket.tournament.dto.BowlerFiguresResponse;
import com.cricket.tournament.service.BallDeliveryService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/innings/{inningsId}/bowlers")
public class BowlerFiguresController {

    private final BallDeliveryService ballDeliveryService;

    public BowlerFiguresController(
            BallDeliveryService ballDeliveryService) {
        this.ballDeliveryService = ballDeliveryService;
    }

    @GetMapping("/{bowlerId}/figures")
    public BowlerFiguresResponse getBowlerFigures(
            @PathVariable Long inningsId,
            @PathVariable Long bowlerId) {

        return ballDeliveryService.getBowlerFigures(
                inningsId, bowlerId);
    }
}