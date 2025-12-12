package com.fitness_club.web.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class WorkoutLogResponse {
    private UUID id;
    private String workoutName;
    private int duration;
    private LocalDateTime dateCompleted;
}