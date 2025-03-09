package com.fitness_club.fitnessPlan.model;

import com.fitness_club.user.model.User;
import com.fitness_club.workout.model.Difficulty;
import com.fitness_club.workout.model.Workout;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class FitnessPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "fitnessPlan")
    private List<Workout> workouts = new ArrayList<>();

}
