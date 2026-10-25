package com.cricket.tournament.service;

import com.cricket.tournament.dto.BatterFiguresResponse;
import com.cricket.tournament.entity.BallDelivery;
import com.cricket.tournament.entity.Player;
import com.cricket.tournament.repository.BallDeliveryRepository;
import com.cricket.tournament.repository.InningsRepository;
import com.cricket.tournament.repository.PlayerRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
@Service
public class BatterFiguresService {

private final BallDeliveryRepository deliveryRepository;
private final InningsRepository inningsRepository;
private final PlayerRepository playerRepository;

public BatterFiguresService(
        BallDeliveryRepository deliveryRepository,
        InningsRepository inningsRepository,
        PlayerRepository playerRepository) {

    this.deliveryRepository = deliveryRepository;
    this.inningsRepository = inningsRepository;
    this.playerRepository = playerRepository;
}

@Transactional(readOnly = true)
public BatterFiguresResponse getBatterFigures(
        Long inningsId,
        Long batterId) {

    // 1. Verify that the innings exists.
    if (!inningsRepository.existsById(inningsId)) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Innings not found with ID: " + inningsId);
    }

    // 2. Find the batter.
    Player batter = playerRepository.findById(batterId)
            .orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Batter not found with ID: " + batterId));

    // 3. Retrieve the innings deliveries.
    List<BallDelivery> deliveries =
            deliveryRepository
                    .findByInningsIdOrderByDeliveryNumberAsc(inningsId);

    int runs = 0;
    int ballsFaced = 0;
    int fours = 0;
    int sixes = 0;

    boolean out = false;
    String dismissalType = null;

    // 4. Calculate the batter's figures.
    for (BallDelivery delivery : deliveries) {

        // Count only deliveries faced by this batter.
        if (!delivery.getStriker().getId().equals(batterId)) {
            continue;
        }

        // Batter's runs come from the bat, not extras.
        runs += delivery.getBatRuns();

        // Wides do not count as balls faced.
        // No-balls count as balls faced in this implementation.
        if (delivery.getWideRuns() == 0) {
            ballsFaced++;
        }

        // Count boundaries scored from the bat.
        if (delivery.getBatRuns() == 4) {
            fours++;
        }

        if (delivery.getBatRuns() == 6) {
            sixes++;
        }

        // Track dismissals of this batter.
        if (delivery.isWicket()
                && delivery.getDismissedPlayer() != null
                && delivery.getDismissedPlayer()
                        .getId().equals(batterId)) {

            out = true;
            dismissalType = delivery.getDismissalType();
        }
    }

    // 5. Calculate strike rate.
    double strikeRate = ballsFaced == 0
            ? 0.0
            : (runs * 100.0) / ballsFaced;

    // 6. Build the response.
    BatterFiguresResponse response =
            new BatterFiguresResponse();

    response.setBatterId(batter.getId());
    response.setBatterName(batter.getName());
    response.setRuns(runs);
    response.setBallsFaced(ballsFaced);
    response.setFours(fours);
    response.setSixes(sixes);
    response.setStrikeRate(
            Math.round(strikeRate * 100.0) / 100.0);
    response.setOut(out);
    response.setDismissalType(dismissalType);

    return response;
	}

@Transactional(readOnly = true)
public List<BatterFiguresResponse> getBattingScorecard(Long inningsId) {


// 1. Verify that the innings exists.
var innings = inningsRepository.findById(inningsId)
        .orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Innings not found with ID: " + inningsId));

// 2. Collect batters in their order of appearance.
Set<Long> batterIds = new LinkedHashSet<>();

// Include the currently assigned batters first.
if (innings.getStriker() != null) {
    batterIds.add(innings.getStriker().getId());
}

if (innings.getNonStriker() != null) {
    batterIds.add(innings.getNonStriker().getId());
}

// 3. Include batters found in the delivery history.
List<BallDelivery> deliveries =
        deliveryRepository.findByInningsIdOrderByDeliveryNumberAsc(
                inningsId);

for (BallDelivery delivery : deliveries) {

    batterIds.add(delivery.getStriker().getId());
    batterIds.add(delivery.getNonStriker().getId());

    if (delivery.getDismissedPlayer() != null) {
        batterIds.add(delivery.getDismissedPlayer().getId());
    }
}

// 4. Calculate figures for every collected batter.
List<BatterFiguresResponse> scorecard = new ArrayList<>();

for (Long batterId : batterIds) {
    scorecard.add(getBatterFigures(inningsId, batterId));
}

return scorecard;


}




}
