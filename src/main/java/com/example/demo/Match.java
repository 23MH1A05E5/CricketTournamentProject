package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String team1;
    private String team2;

    private Integer team1Score;
    private Integer team2Score;
    private String winner;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTeam1() { return team1; }
    public void setTeam1(String team1) { this.team1 = team1; }

    public String getTeam2() { return team2; }
    public void setTeam2(String team2) { this.team2 = team2; }

    public Integer getTeam1Score() { return team1Score; }
    public void setTeam1Score(Integer team1Score) { this.team1Score = team1Score; }

    public Integer getTeam2Score() { return team2Score; }
    public void setTeam2Score(Integer team2Score) { this.team2Score = team2Score; }

    public String getWinner() { return winner; }
    public void setWinner(String winner) { this.winner = winner; }
}