package com.fitness_club.medal.model;

import com.fitness_club.user.model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Medal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rarity rarity;

    @Column(nullable = false)
    private LocalDateTime earnedDate;

    @ManyToOne
    private User user;

}
