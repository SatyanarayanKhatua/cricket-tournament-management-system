package com.cricket.tournament.controller;

import com.cricket.tournament.dto.BallDeliveryRequest;
import com.cricket.tournament.dto.BallDeliveryResponse;
import com.cricket.tournament.service.BallDeliveryService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/innings/{inningsId}/deliveries")
public class BallDeliveryController {

    private final BallDeliveryService ballDeliveryService;

    public BallDeliveryController(
            BallDeliveryService ballDeliveryService) {
        this.ballDeliveryService = ballDeliveryService;
    }

    // Record a delivery
    @PostMapping
    public BallDeliveryResponse recordDelivery(
            @PathVariable Long inningsId,
            @Valid @RequestBody BallDeliveryRequest request) {

        return ballDeliveryService.recordDelivery(
                inningsId, request);
    }

    // Retrieve all deliveries for an innings
    @GetMapping
    public List<BallDeliveryResponse> getDeliveries(
            @PathVariable Long inningsId) {

        return ballDeliveryService.getDeliveries(inningsId);
    }
}

