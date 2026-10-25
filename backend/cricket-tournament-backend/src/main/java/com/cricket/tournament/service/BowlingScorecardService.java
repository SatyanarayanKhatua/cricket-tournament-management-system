package com.cricket.tournament.service;

import com.cricket.tournament.dto.BowlerFiguresResponse;
import com.cricket.tournament.entity.BallDelivery;
import com.cricket.tournament.repository.BallDeliveryRepository;
import com.cricket.tournament.repository.InningsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class BowlingScorecardService {

private final BallDeliveryRepository deliveryRepository;
private final InningsRepository inningsRepository;
private final BallDeliveryService ballDeliveryService;

public BowlingScorecardService(
        BallDeliveryRepository deliveryRepository,
        InningsRepository inningsRepository,
        BallDeliveryService ballDeliveryService) {
    this.deliveryRepository = deliveryRepository;
    this.inningsRepository = inningsRepository;
    this.ballDeliveryService = ballDeliveryService;
}

@Transactional(readOnly = true)
public List<BowlerFiguresResponse> getBowlingScorecard(Long inningsId) {

    if (!inningsRepository.existsById(inningsId)) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Innings not found with ID: " + inningsId);
    }

    List<BallDelivery> deliveries =
            deliveryRepository.findByInningsIdOrderByDeliveryNumberAsc(inningsId);

    Set<Long> bowlerIds = new LinkedHashSet<>();

    for (BallDelivery delivery : deliveries) {
        bowlerIds.add(delivery.getBowler().getId());
    }

    List<BowlerFiguresResponse> scorecard = new ArrayList<>();

    for (Long bowlerId : bowlerIds) {
        scorecard.add(
                ballDeliveryService.getBowlerFigures(inningsId, bowlerId));
    }

    return scorecard;
}


}
