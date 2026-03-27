package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
        // Add 11 empty players
        for (int i = 0; i < 11; i++) {
            team.getPlayers().add(new Player());
        }
        model.addAttribute("team", team);
        return "registerteam";
    }

    @PostMapping("/registerteam")
    public String registerTeam(@ModelAttribute Team team, Model model) {
        teamRepo.save(team);

        // If two teams exist, generate a match
        if (teamRepo.count() % 2 == 0) {
            List<Team> teams = teamRepo.findAll();
            int size = teams.size();
            Team t1 = teams.get(size - 2);
            Team t2 = teams.get(size - 1);

            Match match = new Match();
            match.setTeam1(t1.getTeamName());
            match.setTeam2(t2.getTeamName());
            matchRepo.save(match);

            model.addAttribute("match", match);
            return "match";
        }

        return "success";
    }
}