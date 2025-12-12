package com.fitness_club.client;

import com.fitness_club.web.dto.WorkoutLogRequest;
import com.fitness_club.web.dto.WorkoutLogResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "history-service", url = "http://localhost:8081/api/history")
public interface HistoryClient {

    @PostMapping
    void logWorkout(@RequestBody WorkoutLogRequest request);

    @GetMapping("/user/{userId}")
    List<WorkoutLogResponse> getUserHistory(@PathVariable("userId") UUID userId);


}