package com.cricket.tournament.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cricket.tournament.entity.Match;

public interface MatchRepository extends JpaRepository<Match, Long> {

    // Find all matches in a tournament
    List<Match> findByTournamentId(Long tournamentId);

    // Find all matches involving a particular team
    List<Match> findByTeam1IdOrTeam2Id(Long team1Id, Long team2Id);
}