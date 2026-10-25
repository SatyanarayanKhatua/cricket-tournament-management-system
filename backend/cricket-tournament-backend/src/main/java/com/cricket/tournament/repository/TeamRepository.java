package com.cricket.tournament.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cricket.tournament.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findByTournamentId(Long tournamentId);

    boolean existsByNameIgnoreCaseAndTournamentId(
            String name,
            Long tournamentId);
}