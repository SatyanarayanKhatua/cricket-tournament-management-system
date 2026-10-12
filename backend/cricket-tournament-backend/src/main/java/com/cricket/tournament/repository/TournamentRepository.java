package com.cricket.tournament.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cricket.tournament.entity.Tournament;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {

}