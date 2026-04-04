package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TournamentService {

    @Autowired
    private MatchRepo matchRepo;

    public Match recordResult(Long matchId, Integer team1Score, Integer team2Score) {
        Match match = matchRepo.findById(matchId).orElseThrow();

        match.setTeam1Score(team1Score);
        match.setTeam2Score(team2Score);

        if (team1Score > team2Score) {
            match.setWinner(match.getTeam1());
        } else if (team2Score > team1Score) {
            match.setWinner(match.getTeam2());
        } else {
            match.setWinner("Draw");
        }

        matchRepo.save(match);
        return match;
    }

    public List<Match> getAllMatches() {
        return matchRepo.findAll();
    }

    public List<String> getWinningTeams() {
        return matchRepo.findAll().stream()
                .map(Match::getWinner)
                .filter(w -> w != null && !w.equals("Draw"))
                .collect(Collectors.toList());
    }

    public Match generateNextMatch() {
        List<String> winners = getWinningTeams();
        if (winners.size() >= 2) {
            String teamA = winners.get(winners.size() - 2);
            String teamB = winners.get(winners.size() - 1);

            Match nextMatch = new Match();
            nextMatch.setTeam1(teamA);
            nextMatch.setTeam2(teamB);

            return matchRepo.save(nextMatch);
        }
        return null;
    }

    public String getLatestMatchWinner() {
        return matchRepo.findAll().stream()
                .filter(m -> m.getWinner() != null && !m.getWinner().equals("Draw"))
                .reduce((first, second) -> second)
                .map(Match::getWinner)
                .orElse(null);
    }
}