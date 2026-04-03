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
        // If validation fails (like age < 18), reload form
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
        Match match = matchRepo.findById(id).orElseThrow();
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
        model.addAttribute("match", match);
        return "matchresult";
    }
}