package com.cricket.tournament.service;

import com.cricket.tournament.dto.BallDeliveryRequest;

import com.cricket.tournament.dto.BallDeliveryResponse;
import com.cricket.tournament.entity.BallDelivery;
import com.cricket.tournament.entity.Innings;
import com.cricket.tournament.entity.Player;
import com.cricket.tournament.repository.BallDeliveryRepository;
import com.cricket.tournament.repository.InningsRepository;
import com.cricket.tournament.repository.PlayerRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
import com.cricket.tournament.dto.BowlerFiguresResponse;
@Service
public class BallDeliveryService {

    private final BallDeliveryRepository deliveryRepository;
    private final InningsRepository inningsRepository;
    private final PlayerRepository playerRepository;
    private final MatchResultService matchResultService;


    public BallDeliveryService(
            BallDeliveryRepository deliveryRepository,
            InningsRepository inningsRepository,
            PlayerRepository playerRepository,
            MatchResultService matchResultService) {

        this.deliveryRepository = deliveryRepository;
        this.inningsRepository = inningsRepository;
        this.playerRepository = playerRepository;
        this.matchResultService = matchResultService;

    }

    @Transactional
    public BallDeliveryResponse recordDelivery(
            Long inningsId,
            BallDeliveryRequest request) {

        // 1. Find the innings.
        Innings innings = inningsRepository.findById(inningsId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Innings not found with ID: " + inningsId));

        // 2. Only an active innings can receive deliveries.
        if (!"IN_PROGRESS".equalsIgnoreCase(innings.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot record a delivery because the innings is not in progress");
        }
        
        if (innings.isBowlerSelectionRequired()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Select the next bowler before recording another delivery");
        }

        // 3. Validate scoring inputs before changing anything.
        validateScoringRequest(request);

     // 4. Get the current batters from the innings state.
        Player striker = innings.getStriker();
        Player nonStriker = innings.getNonStriker();

        // The innings must have both batters assigned.
        if (striker == null || nonStriker == null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "The innings must have a striker and non-striker before recording a delivery");
        }

        // 5. Ensure the request matches the current batting state.
        if (!striker.getId().equals(request.getStrikerId())
                || !nonStriker.getId().equals(request.getNonStrikerId())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "The requested batters do not match the current innings state");
        }

     // Load the bowler requested for this delivery.
     Player bowler = findPlayer(request.getBowlerId());

     // Validate that a bowler has been selected for the current over.
     Player currentBowler = innings.getCurrentBowler();

     if (currentBowler == null) {
         throw new ResponseStatusException(
                 HttpStatus.CONFLICT,
                 "No bowler has been selected for the current over");
     }

     // Ensure the requested bowler matches the currently selected bowler.
     if (!currentBowler.getId().equals(bowler.getId())) {
         throw new ResponseStatusException(
                 HttpStatus.CONFLICT,
                 "The requested bowler does not match the currently selected bowler");
     }
     
        // 5. Validate the batting and bowling teams.
        if (striker.getId().equals(nonStriker.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Striker and non-striker must be different players");
        }

        if (!striker.getTeam().getId()
                .equals(innings.getBattingTeam().getId())
                || !nonStriker.getTeam().getId()
                .equals(innings.getBattingTeam().getId())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Both batters must belong to the batting team");
        }

        if (!bowler.getTeam().getId()
                .equals(innings.getBowlingTeam().getId())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Bowler must belong to the bowling team");
        }

        
     // 6. Validate the wicket and dismissed player.
     Player dismissedPlayer = null;
     Player fielder = null;
     Player incomingBatter = null;

     if (request.isWicket()) {

         dismissedPlayer = findPlayer(request.getDismissedPlayerId());

         if (!dismissedPlayer.getId().equals(striker.getId())
                 && !dismissedPlayer.getId().equals(nonStriker.getId())) {

             throw new ResponseStatusException(
                     HttpStatus.BAD_REQUEST,
                     "The dismissed player must be the striker or non-striker");
         }

         if (request.getDismissalType() == null
                 || request.getDismissalType().isBlank()) {

             throw new ResponseStatusException(
                     HttpStatus.BAD_REQUEST,
                     "Dismissal type is required for a wicket");
         }

         String dismissalType = request.getDismissalType()
                 .trim().toUpperCase(Locale.ROOT);

         if (!isSupportedDismissal(dismissalType)) {
             throw new ResponseStatusException(
                     HttpStatus.BAD_REQUEST,
                     "Unsupported dismissal type: " + dismissalType);
         }

         if (requiresFielder(dismissalType)
                 && request.getFielderId() == null) {

             throw new ResponseStatusException(
                     HttpStatus.BAD_REQUEST,
                     "A fielder is required for this dismissal type");
         }

         if (request.getFielderId() != null) {

             fielder = findPlayer(request.getFielderId());

             if (!fielder.getTeam().getId()
                     .equals(innings.getBowlingTeam().getId())) {

                 throw new ResponseStatusException(
                         HttpStatus.BAD_REQUEST,
                         "Fielder must belong to the bowling team");
             }
         }

      // Validate the incoming batter if one is provided.
         if (request.getIncomingBatterId() != null) {

             incomingBatter = findPlayer(request.getIncomingBatterId());

             // Incoming batter must belong to the batting team.
             if (!incomingBatter.getTeam().getId()
                     .equals(innings.getBattingTeam().getId())) {

                 throw new ResponseStatusException(
                         HttpStatus.BAD_REQUEST,
                         "Incoming batter must belong to the batting team");
             }

             // Incoming batter cannot already be at the crease.
             if (incomingBatter.getId().equals(striker.getId())
                     || incomingBatter.getId().equals(nonStriker.getId())) {

                 throw new ResponseStatusException(
                         HttpStatus.BAD_REQUEST,
                         "Incoming batter is already at the crease");
             }

             // Incoming batter cannot be the player dismissed on this delivery.
             if (incomingBatter.getId().equals(dismissedPlayer.getId())) {

                 throw new ResponseStatusException(
                         HttpStatus.BAD_REQUEST,
                         "Incoming batter cannot be the dismissed player");
             }

          // Check whether the incoming batter was dismissed previously.
             Long incomingBatterId = incomingBatter.getId();

             boolean alreadyDismissed = deliveryRepository
                     .findByInningsIdOrderByDeliveryNumberAsc(inningsId)
                     .stream()
                     .anyMatch(previousDelivery ->
                             previousDelivery.getDismissedPlayer() != null
                                     && previousDelivery.getDismissedPlayer()
                                             .getId()
                                             .equals(incomingBatterId)
                                     && !"RETIRED_HURT".equalsIgnoreCase(
                                             previousDelivery.getDismissalType()));

             if (alreadyDismissed) {
                 throw new ResponseStatusException(
                         HttpStatus.BAD_REQUEST,
                         "A previously dismissed batter cannot return to the crease");
             }
            
         }
         

     } else if (request.getDismissedPlayerId() != null
             || request.getDismissalType() != null
             || request.getFielderId() != null
             || request.getIncomingBatterId() != null) {

         throw new ResponseStatusException(
                 HttpStatus.BAD_REQUEST,
                 "Dismissal details cannot be provided when wicket is false");
     }
     	

        // 7. Calculate legal delivery status.
        boolean legalDelivery =
                request.getWideRuns() == 0
                && request.getNoBallRuns() == 0;

        // 8. Calculate runs. The frontend does not supply totalRuns.
        int extras = request.getWideRuns()
                + request.getNoBallRuns()
                + request.getByeRuns()
                + request.getLegByeRuns()
                + request.getPenaltyRuns();

        int totalRuns = request.getBatRuns() + extras;

     // 9. Get the next delivery number from the innings state.
        int deliveryNumber = innings.getNextDeliveryNumber();

        // 10. Create the delivery record.
        BallDelivery delivery = new BallDelivery();

        delivery.setInnings(innings);
        delivery.setDeliveryNumber(deliveryNumber);

        delivery.setStriker(striker);
        delivery.setNonStriker(nonStriker);
        delivery.setBowler(bowler);
        
        delivery.setStrikerBefore(innings.getStriker());
        delivery.setNonStrikerBefore(innings.getNonStriker());
        delivery.setBowlerBefore(innings.getCurrentBowler());
        delivery.setLegalBallsBefore(innings.getLegalBalls());

        delivery.setBatRuns(request.getBatRuns());
        delivery.setWideRuns(request.getWideRuns());
        delivery.setNoBallRuns(request.getNoBallRuns());
        delivery.setByeRuns(request.getByeRuns());
        delivery.setLegByeRuns(request.getLegByeRuns());
        delivery.setPenaltyRuns(request.getPenaltyRuns());

        delivery.setTotalRuns(totalRuns);
        delivery.setLegalDelivery(legalDelivery);

        delivery.setWicket(request.isWicket());
        delivery.setBattersCrossed(request.getBattersCrossed());
        delivery.setRunOutEnd(request.getRunOutEnd());
        delivery.setDismissalType(
                request.isWicket()
                        ? request.getDismissalType()
                                .trim().toUpperCase(Locale.ROOT)
                        : null);
     // Determine whether this dismissal is credited to the bowler.
        boolean bowlerWicket = request.isWicket()
                && countsAsBowlerWicket(request.getDismissalType());

        delivery.setBowlerWicket(bowlerWicket);

        // Calculate the runs charged to the bowler.
        int bowlerRunsConceded = request.getBatRuns()
                + request.getWideRuns()
                + request.getNoBallRuns();

        delivery.setBowlerRunsConceded(bowlerRunsConceded);

        delivery.setDismissedPlayer(dismissedPlayer);
        delivery.setFielder(fielder);
        delivery.setCommentary(request.getCommentary());

        BallDelivery savedDelivery =
                deliveryRepository.save(delivery);
     // Store the bowler currently delivering the ball.
        innings.setCurrentBowler(bowler);
     // Prepare the delivery number for the next ball.
        innings.setNextDeliveryNumber(deliveryNumber + 1);

        // 11. Update the innings totals in the same transaction.
        innings.setTotalRuns(innings.getTotalRuns() + totalRuns);

        if (request.isWicket()
                && countsAsTeamWicket(
                        request.getDismissalType())) {

            innings.setWickets(innings.getWickets() + 1);
        }

        if (legalDelivery) {
            innings.setLegalBalls(innings.getLegalBalls() + 1);
        }
        
     // 11. Calculate completed runs and determine over completion.
        int completedRuns = request.getBatRuns()
                + request.getByeRuns()
                + request.getLegByeRuns();

        boolean overFinished = legalDelivery
                && innings.getLegalBalls() > 0
                && innings.getLegalBalls() % 6 == 0;

        boolean runOut = request.isWicket()
                && "RUN_OUT".equalsIgnoreCase(request.getDismissalType());

        // For normal deliveries, rotate strike for an odd number
        // of completed runs.
        //
        // For run-outs, postpone the final batter-position calculation
        // until the dismissed batter and crossing information are considered.
        if (!runOut && completedRuns % 2 != 0) {
            Player currentStriker = innings.getStriker();

            innings.setStriker(innings.getNonStriker());
            innings.setNonStriker(currentStriker);
        }
     
        // 12. Check whether the innings has reached its limits.
        boolean oversFinished =
                innings.getOversLimit() != null
                && innings.getLegalBalls()
                        >= innings.getOversLimit() * 6;

        boolean wicketsFinished = innings.getWickets() >= 10;

        boolean targetReached = false;

        // During innings 2, calculate the target from innings 1.
        if (innings.getInningsNumber() == 2) {

            Innings firstInnings = inningsRepository
                    .findByMatchIdOrderByInningsNumberAsc(
                            innings.getMatch().getId())
                    .stream()
                    .filter(existing ->
                            existing.getInningsNumber() == 1)
                    .findFirst()
                    .orElseThrow(() ->
                            new ResponseStatusException(
                                    HttpStatus.CONFLICT,
                                    "First innings not found; cannot calculate target"));

            int target = firstInnings.getTotalRuns() + 1;

            targetReached = innings.getTotalRuns() >= target;
        }

     // Determine whether the innings has ended.
        boolean inningsCompleted =
                targetReached || oversFinished || wicketsFinished;

        // Require a replacement batter only if the innings continues.
        if (request.isWicket()
                && !inningsCompleted
                && incomingBatter == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Incoming batter ID is required because the innings continues");
        }

        // Mark the innings as completed when any limit is reached.
        if (inningsCompleted) {
            innings.setStatus("COMPLETED");
        }
        if (overFinished && !inningsCompleted) {
            innings.setBowlerSelectionRequired(true);
        }
     // Replace the dismissed batter and handle end-of-over rotation.
        if (request.isWicket() && !inningsCompleted) {

            boolean dismissedWasStriker =
                    dismissedPlayer.getId().equals(striker.getId());

            if (runOut) {

                /*
                 * The saved striker and non-striker represent the original
                 * positions at the start of this delivery.
                 *
                 * First determine the positions after the completed runs.
                 */
                Player strikerAtDismissal = striker;
                Player nonStrikerAtDismissal = nonStriker;

                if (completedRuns % 2 != 0) {
                    strikerAtDismissal = nonStriker;
                    nonStrikerAtDismissal = striker;
                }

                /*
                 * If the batters crossed during an additional,
                 * unfinished run before the wicket was broken,
                 * their positions may have changed again.
                 *
                 * This Boolean-based handling is intended for the
                 * current MVP and does not cover every possible
                 * multi-run scenario.
                 */
                if (Boolean.TRUE.equals(request.getBattersCrossed())) {
                    Player currentStriker = strikerAtDismissal;

                    strikerAtDismissal = nonStrikerAtDismissal;
                    nonStrikerAtDismissal = currentStriker;
                }

                /*
                 * Replace the dismissed batter at the position
                 * they occupied when the wicket was broken.
                 */
                if (dismissedPlayer.getId()
                        .equals(strikerAtDismissal.getId())) {

                    innings.setStriker(incomingBatter);
                    innings.setNonStriker(nonStrikerAtDismissal);

                } else if (dismissedPlayer.getId()
                        .equals(nonStrikerAtDismissal.getId())) {

                    innings.setStriker(strikerAtDismissal);
                    innings.setNonStriker(incomingBatter);

                } else {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Dismissed batter position could not be determined");
                }

            } else {

                /*
                 * For other dismissals, the incoming batter replaces
                 * the dismissed batter at the appropriate end.
                 *
                 * The normal completed-run rotation has already
                 * been applied above.
                 */
                if (dismissedWasStriker) {
                    innings.setStriker(incomingBatter);
                } else {
                    innings.setNonStriker(incomingBatter);
                }
            }

         
        }
        // Rotate strike at the end of every completed over,
        // regardless of whether a wicket occurred.
        if (overFinished && !inningsCompleted) {
        	Player currentStriker = innings.getStriker();

        	innings.setStriker(innings.getNonStriker());
        	innings.setNonStriker(currentStriker);
        }

        inningsRepository.save(innings);

        // Calculate the match result when the second innings finishes.
        if (innings.getInningsNumber() == 2
                && "COMPLETED".equalsIgnoreCase(innings.getStatus())) {

            matchResultService.calculateAndSaveResult(
                    innings.getMatch().getId());
        }

        BallDeliveryResponse response = convertToResponse(savedDelivery);

        response.setOverFinished(overFinished);

        response.setBowlerSelectionRequired(
                overFinished && !inningsCompleted
        );

        return response;
        
    	}

    private Player findPlayer(Long playerId) {
        if (playerId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Player ID is required");
        }

        return playerRepository.findById(playerId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Player not found with ID: " + playerId));
    }

    private void validateScoringRequest(BallDeliveryRequest request) {

        if (request.getBatRuns() < 0 || request.getBatRuns() > 6
                || request.getWideRuns() < 0
                || request.getNoBallRuns() < 0
                || request.getByeRuns() < 0
                || request.getLegByeRuns() < 0
                || request.getPenaltyRuns() < 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Run values must be non-negative, and bat runs must be between 0 and 6");
        }

        boolean wide = request.getWideRuns() > 0;
        boolean noBall = request.getNoBallRuns() > 0;
        boolean bye = request.getByeRuns() > 0;
        boolean legBye = request.getLegByeRuns() > 0;

        if (wide && (noBall || bye || legBye || request.getBatRuns() > 0)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Wide runs cannot be combined with bat runs, no-ball runs, byes, or leg-byes");
        }

        if (bye && legBye) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Byes and leg-byes cannot both be recorded on the same delivery");
        }

        if ((bye || legBye) && request.getBatRuns() > 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Byes and leg-byes cannot be combined with bat runs");
        }

        if (noBall && (bye || legBye)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "This MVP does not yet support byes or leg-byes on a no-ball");
        }

        if (request.getPenaltyRuns() > 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Penalty-run scoring is not yet supported by this MVP");
        }

        if (request.getDismissalType() != null
                && !request.isWicket()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Dismissal type requires wicket=true");
        }

        if (request.isWicket()
                && request.getDismissedPlayerId() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Dismissed player ID is required for a wicket");
        }

        if (request.isWicket()
                && (wide || noBall)
                && !"RUN_OUT".equalsIgnoreCase(
                        request.getDismissalType())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only run out is supported for wickets on wides or no-balls in this MVP");
        }
     // Validate the run-out end.
        String runOutEnd = request.getRunOutEnd();

        if (request.isWicket()
                && "RUN_OUT".equalsIgnoreCase(request.getDismissalType())) {

            if (runOutEnd == null || runOutEnd.isBlank()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Run-out end is required for a run-out dismissal");
            }

            if (!"STRIKER_END".equalsIgnoreCase(runOutEnd)
                    && !"NON_STRIKER_END".equalsIgnoreCase(runOutEnd)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Run-out end must be STRIKER_END or NON_STRIKER_END");
            }

        } else if (runOutEnd != null && !runOutEnd.isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Run-out end can only be provided for a run-out dismissal");
        }
    }

    private boolean isSupportedDismissal(String type) {
        if (type == null || type.isBlank()) {
            return false;
        }

        return switch (type.trim().toUpperCase(Locale.ROOT)) {
            case "BOWLED",
                 "CAUGHT",
                 "LBW",
                 "RUN_OUT",
                 "STUMPED",
                 "HIT_WICKET",
                 "OBSTRUCTING_THE_FIELD",
                 "HIT_THE_BALL_TWICE",
                 "TIMED_OUT",
                 "RETIRED_OUT",
                 "RETIRED_HURT" -> true;

            default -> false;
        };
    }

    private boolean requiresFielder(String type) {
        return switch (type) {
            case "CAUGHT",
                 "RUN_OUT",
                 "STUMPED",
                 "OBSTRUCTING_THE_FIELD" -> true;

            default -> false;
        };
    }

    private boolean countsAsTeamWicket(String type) {
        if (type == null) {
            return false;
        }

        return switch (type.trim().toUpperCase(Locale.ROOT)) {
            case "BOWLED",
                 "CAUGHT",
                 "LBW",
                 "RUN_OUT",
                 "STUMPED",
                 "HIT_WICKET",
                 "OBSTRUCTING_THE_FIELD",
                 "HIT_THE_BALL_TWICE",
                 "TIMED_OUT",
                 "RETIRED_OUT" -> true;

            case "RETIRED_HURT" -> false;

            default -> false;
        };
    }

    private BallDeliveryResponse convertToResponse(
            BallDelivery delivery) {

        BallDeliveryResponse response = new BallDeliveryResponse();

        response.setId(delivery.getId());
        response.setInningsId(delivery.getInnings().getId());
        response.setDeliveryNumber(delivery.getDeliveryNumber());

        response.setStrikerId(delivery.getStriker().getId());
        response.setStrikerName(delivery.getStriker().getName());

        response.setNonStrikerId(delivery.getNonStriker().getId());
        response.setNonStrikerName(delivery.getNonStriker().getName());

        response.setBowlerId(delivery.getBowler().getId());
        response.setBowlerName(delivery.getBowler().getName());

        response.setBatRuns(delivery.getBatRuns());
        response.setWideRuns(delivery.getWideRuns());
        response.setNoBallRuns(delivery.getNoBallRuns());
        response.setByeRuns(delivery.getByeRuns());
        response.setLegByeRuns(delivery.getLegByeRuns());
        response.setPenaltyRuns(delivery.getPenaltyRuns());

        int totalExtras = delivery.getWideRuns()
                + delivery.getNoBallRuns()
                + delivery.getByeRuns()
                + delivery.getLegByeRuns()
                + delivery.getPenaltyRuns();

        response.setTotalExtras(totalExtras);
        response.setTotalRuns(delivery.getTotalRuns());
        
     // Map delivery legality from the saved entity.
        response.setLegalDelivery(delivery.isLegalDelivery());

        response.setWicket(delivery.isWicket());
        response.setBattersCrossed(delivery.getBattersCrossed());
        response.setRunOutEnd(delivery.getRunOutEnd());
        response.setDismissalType(delivery.getDismissalType());

        if (delivery.getDismissedPlayer() != null) {
            response.setDismissedPlayerId(
                    delivery.getDismissedPlayer().getId());
            response.setDismissedPlayerName(
                    delivery.getDismissedPlayer().getName());
        }

        if (delivery.getFielder() != null) {
            response.setFielderId(delivery.getFielder().getId());
            response.setFielderName(delivery.getFielder().getName());
        }

        response.setCommentary(delivery.getCommentary());

        return response;
    }
    
    @Transactional(readOnly = true)
    public BowlerFiguresResponse getBowlerFigures(
            Long inningsId,
            Long bowlerId) {

        // Verify that the innings exists.
        if (!inningsRepository.existsById(inningsId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Innings not found with ID: " + inningsId);
        }

        // Verify that the bowler exists.
        Player bowler = findPlayer(bowlerId);

        // Retrieve all recorded deliveries for the innings.
        var deliveries =
                deliveryRepository
                        .findByInningsIdOrderByDeliveryNumberAsc(inningsId);

        int legalBalls = 0;
        int runsConceded = 0;
        int wickets = 0;

        for (BallDelivery delivery : deliveries) {

            // Count only deliveries bowled by the requested bowler.
            if (!delivery.getBowler().getId().equals(bowlerId)) {
                continue;
            }

            // Wides and no-balls do not count as legal balls.
            if (delivery.isLegalDelivery()) {
                legalBalls++;
            }

            // Byes and leg-byes are not charged to the bowler.
            runsConceded += delivery.getBatRuns()
                    + delivery.getWideRuns()
                    + delivery.getNoBallRuns();

            // Count only wickets credited to the bowler.
            if (delivery.isBowlerWicket()) {
                wickets++;
            }
        }

        // Calculate economy without dividing by zero.
        double economy = legalBalls == 0
                ? 0.0
                : (runsConceded * 6.0) / legalBalls;

        BowlerFiguresResponse response = new BowlerFiguresResponse();

        response.setBowlerId(bowler.getId());
        response.setBowlerName(bowler.getName());
        response.setLegalBalls(legalBalls);
        response.setRunsConceded(runsConceded);
        response.setWickets(wickets);
        response.setEconomy(Math.round(economy * 100.0) / 100.0);

        return response;
    }
    private boolean countsAsBowlerWicket(String dismissalType) {
        if (dismissalType == null || dismissalType.isBlank()) {
            return false;
        }

        return switch (dismissalType.trim().toUpperCase(Locale.ROOT)) {
            case "BOWLED",
                 "CAUGHT",
                 "LBW",
                 "STUMPED",
                 "HIT_WICKET" -> true;

            case "RUN_OUT",
                 "OBSTRUCTING_THE_FIELD",
                 "HIT_THE_BALL_TWICE",
                 "TIMED_OUT",
                 "RETIRED_OUT",
                 "RETIRED_HURT" -> false;

            default -> false;
        };
    }
   
    @Transactional(readOnly = true)
    public java.util.List<BallDeliveryResponse> getDeliveries(Long inningsId) {

        if (!inningsRepository.existsById(inningsId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Innings not found with ID: " + inningsId
            );
        }

        return deliveryRepository
                .findByInningsIdOrderByDeliveryNumberAsc(inningsId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }
    

}