package com.cricket.tournament.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cricket.tournament.entity.Tournament;
import com.cricket.tournament.repository.TournamentRepository;
import com.cricket.tournament.exception.ResourceNotFoundException;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;

    public TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    public Tournament createTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public Tournament getTournamentById(Long id) {
        return tournamentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Tournament not found with ID: " + id));
    }
    public Tournament updateTournament(Long id, Tournament updatedTournament) {

        Tournament existingTournament = tournamentRepository.findById(id)
        		.orElseThrow(() ->
                new ResourceNotFoundException(
                        "Tournament not found with ID: " + id));
        
        existingTournament.setName(updatedTournament.getName());
        existingTournament.setLocation(updatedTournament.getLocation());
        existingTournament.setFormat(updatedTournament.getFormat());
        existingTournament.setStartDate(updatedTournament.getStartDate());
        existingTournament.setEndDate(updatedTournament.getEndDate());

        return tournamentRepository.save(existingTournament);
    }
    public void deleteTournament(Long id) {

        Tournament existingTournament = tournamentRepository.findById(id)
        		.orElseThrow(() ->
                new ResourceNotFoundException(
                        "Tournament not found with ID: " + id));

        tournamentRepository.delete(existingTournament);
    }
    
}