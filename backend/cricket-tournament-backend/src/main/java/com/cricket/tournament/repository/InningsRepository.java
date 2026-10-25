package com.cricket.tournament.repository;

import com.cricket.tournament.entity.Innings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InningsRepository extends JpaRepository<Innings, Long> {

    // Retrieve all innings belonging to a match in innings order.
    List<Innings> findByMatchIdOrderByInningsNumberAsc(Long matchId);

    // Find a particular innings of a match.
    Optional<Innings> findByMatchIdAndInningsNumber(
            Long matchId,
            int inningsNumber
    );

    // Check whether an innings number already exists for a match.
    boolean existsByMatchIdAndInningsNumber(
            Long matchId,
            int inningsNumber
    );

    // Retrieve the most recently created innings for a match.
    Optional<Innings> findTopByMatchIdOrderByInningsNumberDesc(
            Long matchId
    );
}