package com.fitness_club.user.model;

import com.fitness_club.fitnessPlan.model.FitnessPlan;
import com.fitness_club.meal.model.Meal;
import com.fitness_club.medal.model.Medal;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String firstName;

    private String lastName;

    private int age;

    private int height;

    private int weight;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String profilePicture;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Meal> meals = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<FitnessPlan> purchasedPlans = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "user")
    private List<Medal> medals = new ArrayList<>();

}
