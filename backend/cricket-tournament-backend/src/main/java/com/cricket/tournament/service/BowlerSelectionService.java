package com.cricket.tournament.service;

import com.cricket.tournament.dto.EligibleBowlerResponse;
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

import java.util.List;
import java.util.Objects;

@Service
public class BowlerSelectionService {

    private final InningsRepository inningsRepository;
    private final PlayerRepository playerRepository;
    private final BallDeliveryRepository deliveryRepository;

    public BowlerSelectionService(
            InningsRepository inningsRepository,
            PlayerRepository playerRepository,
            BallDeliveryRepository deliveryRepository) {

        this.inningsRepository = inningsRepository;
        this.playerRepository = playerRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @Transactional(readOnly = true)
    public List<EligibleBowlerResponse> getEligibleBowlers(Long inningsId) {

        Innings innings = findInnings(inningsId);

        if (!"IN_PROGRESS".equalsIgnoreCase(innings.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Bowler selection is available only during an active innings");
        }

        if (!innings.isBowlerSelectionRequired()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A new bowler is not required at this time");
        }

        Long previousBowlerId = getPreviousOverBowlerId(inningsId);

        return playerRepository
                .findByTeamId(innings.getBowlingTeam().getId())
                .stream()
                .filter(player ->
                        "BOWLER".equalsIgnoreCase(player.getRole())
                        || "ALL_ROUNDER".equalsIgnoreCase(player.getRole()))
                .filter(player -> previousBowlerId == null
                        || !player.getId().equals(previousBowlerId))
                .map(player -> new EligibleBowlerResponse(
                        player.getId(),
                        player.getName(),
                        player.getRole(),
                        player.getBowlingStyle()))
                .toList();
    }

    @Transactional
    public void selectBowler(Long inningsId, Long bowlerId) {

        if (bowlerId == null || bowlerId <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A valid bowler ID is required");
        }

        Innings innings = inningsRepository.findById(inningsId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Innings not found with ID: " + inningsId));

        if (!"IN_PROGRESS".equalsIgnoreCase(innings.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot select a bowler because the innings is not in progress");
        }

        if (!innings.isBowlerSelectionRequired()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A new bowler is not required at this time");
        }

        Player bowler = playerRepository.findById(bowlerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Player not found with ID: " + bowlerId));

        if (!Objects.equals(
                bowler.getTeam().getId(),
                innings.getBowlingTeam().getId())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selected bowler must belong to the bowling team");
        }

        Long previousBowlerId = getPreviousOverBowlerId(inningsId);

        if (previousBowlerId != null
                && previousBowlerId.equals(bowlerId)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The bowler who bowled the previous over cannot bowl this over");
        }

        innings.setCurrentBowler(bowler);
        innings.setBowlerSelectionRequired(false);

        inningsRepository.save(innings);
    }

    private Innings findInnings(Long inningsId) {

        return inningsRepository.findById(inningsId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Innings not found with ID: " + inningsId));
    }

    private Long getPreviousOverBowlerId(Long inningsId) {

        List<BallDelivery> deliveries =
                deliveryRepository
                        .findByInningsIdOrderByDeliveryNumberAsc(inningsId);

        if (deliveries.isEmpty()) {
            return null;
        }

        BallDelivery lastDelivery = deliveries.get(deliveries.size() - 1);

        return lastDelivery.getBowler().getId();
    }
}