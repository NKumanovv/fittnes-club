package com.fitness_club.meal.model;

import com.fitness_club.user.model.User;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double calories;

    @Column(nullable = false)
    private Double protein;

    @Column(nullable = false)
    private Double carbs;

    @Column(nullable = false)
    private Double fats;

    @Column(nullable = false)
    private boolean isPublic;

    @ManyToOne
    private User user;
}
