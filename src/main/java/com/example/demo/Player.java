package com.example.demo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Min(value = 18, message = "Player must be at least 18 years old")
    private int age;

    private String city;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }
}