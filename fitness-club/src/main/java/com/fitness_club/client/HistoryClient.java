package com.fitness_club.client;

import com.fitness_club.web.dto.WorkoutLogRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "history-service", url = "http://localhost:8081/api/history")
public interface HistoryClient {

    @PostMapping
    void logWorkout(@RequestBody WorkoutLogRequest request);
}