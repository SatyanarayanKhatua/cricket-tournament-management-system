package com.cricket.tournament.dto;

public class TeamResponse {

    private Long id;
    private String name;
    private String shortName;
    private String homeCity;
    private Long tournamentId;
    private String tournamentName;
    private int matchesPlayed;
    private int matchesWon;

    public TeamResponse() {
    }

    public TeamResponse(
            Long id,
            String name,
            String shortName,
            String homeCity,
            Long tournamentId,
            String tournamentName,
            int matchesPlayed,
            int matchesWon) {

        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.homeCity = homeCity;
        this.tournamentId = tournamentId;
        this.tournamentName = tournamentName;
        this.matchesPlayed = matchesPlayed;
        this.matchesWon = matchesWon;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getHomeCity() {
        return homeCity;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public int getMatchesWon() {
        return matchesWon;
    }
}