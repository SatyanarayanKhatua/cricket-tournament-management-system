package com.cricket.tournament.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cricket.tournament.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    // Retrieve all players belonging to a particular team
    List<Player> findByTeamId(Long teamId);
}