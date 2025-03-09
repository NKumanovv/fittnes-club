package com.fitness_club.workout.model;

import com.fitness_club.fitnessPlan.model.FitnessPlan;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Column(nullable = false)
    private int duration;

    @ManyToOne
    private FitnessPlan fitnessPlan;

}
