package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TournamentController {

    @Autowired
    private TeamRepo teamRepo;

    @Autowired
    private MatchRepo matchRepo;

    @Autowired
    private TournamentService tournamentService;

    // Index page
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Registered teams page
    @GetMapping("/teams")
    public String showTeams(Model model) {
        List<Team> teams = teamRepo.findAll();
        model.addAttribute("teams", teams);
        return "teams";
    }

    // Winning teams page
    @GetMapping("/winners")
    public String showWinners(Model model) {
        List<String> winners = tournamentService.getWinningTeams();
        model.addAttribute("winners", winners);
        return "winners";
    }

    @GetMapping("/registerteam")
    public String showTeamForm(Model model) {
        Team team = new Team();
        team.setPlayers(new ArrayList<>());
        for (int i = 0; i < 11; i++) {
            team.getPlayers().add(new Player());
        }
        model.addAttribute("team", team);
        return "registerteam";
    }

    @PostMapping("/registerteam")
    public String registerTeam(@Valid @ModelAttribute Team team,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("team", team);
            return "registerteam";
        }

        for (Player player : team.getPlayers()) {
            player.setTeam(team);
        }
        teamRepo.save(team);

        List<Team> teams = teamRepo.findAll();
        if (teams.size() % 2 == 0) {
            Team t1 = teams.get(teams.size() - 2);
            Team t2 = teams.get(teams.size() - 1);

            Match match = new Match();
            match.setTeam1(t1.getTeamName());
            match.setTeam2(t2.getTeamName());
            matchRepo.save(match);

            model.addAttribute("match", match);
            return "match";
        }

        return "success";
    }

    @PostMapping("/updatematch/{id}")
    public String updateMatchResult(@PathVariable Long id,
                                    @RequestParam Integer team1Score,
                                    @RequestParam Integer team2Score,
                                    Model model) {
        Match match = tournamentService.recordResult(id, team1Score, team2Score);

        List<Match> allMatches = tournamentService.getAllMatches();
        model.addAttribute("allMatches", allMatches);

        List<String> winners = tournamentService.getWinningTeams();
        model.addAttribute("winners", winners);

        Match nextMatch = tournamentService.generateNextMatch();
        model.addAttribute("nextMatch", nextMatch);

        String latestWinner = tournamentService.getLatestMatchWinner();
        model.addAttribute("latestWinner", latestWinner);

        return "winners";
    }

    @GetMapping("/matches")
    public String listMatches(Model model) {
        List<Match> allMatches = matchRepo.findAll();

        List<List<Match>> batches = new ArrayList<>();
        for (int i = 0; i < allMatches.size(); i += 2) {
            int end = Math.min(i + 2, allMatches.size());
            batches.add(allMatches.subList(i, end));
        }

        model.addAttribute("batches", batches);
        return "matches";
    }
}