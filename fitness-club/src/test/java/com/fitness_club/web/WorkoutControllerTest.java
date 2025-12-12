package com.fitness_club.web;

import com.fitness_club.client.HistoryClient;
import com.fitness_club.security.AuthenticationMetadata;
import com.fitness_club.user.model.User;
import com.fitness_club.user.model.UserRole;
import com.fitness_club.user.service.UserService;
import com.fitness_club.web.dto.CreateWorkoutRequest;
import com.fitness_club.web.dto.WorkoutLogRequest;
import com.fitness_club.workout.model.Difficulty;
import com.fitness_club.workout.model.Workout;
import com.fitness_club.workout.service.WorkoutService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkoutController.class)
public class WorkoutControllerTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private WorkoutService workoutService;

    @MockitoBean
    private HistoryClient historyClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getWorkoutsPage_ReturnsViewAndModel() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .username("testuser")
                .role(UserRole.USER) // <--- Fixed: Added Role
                .build();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "testuser", "pass", UserRole.USER, true);

        when(userService.getById(userId)).thenReturn(user);
        when(workoutService.getAllWorkoutsById(userId)).thenReturn(List.of(new Workout()));

        mockMvc.perform(get("/workouts")
                        .with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("workouts"))
                .andExpect(model().attributeExists("user", "workouts"));
    }

    @Test
    void getWorkoutPage_ReturnsWorkoutView() throws Exception {
        UUID workoutId = UUID.randomUUID();
        Workout workout = Workout.builder()
                .id(workoutId)
                .name("Leg Day")
                .difficulty(Difficulty.ADVANCED)
                .duration(60)
                .build();

        when(workoutService.getById(workoutId)).thenReturn(workout);

        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "user", "pass", UserRole.USER, true);

        mockMvc.perform(get("/workouts/{id}", workoutId)
                        .with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("workout"))
                .andExpect(model().attributeExists("workout", "createWorkoutRequest"));
    }

    @Test
    void createWorkout_ValidData_RedirectsToWorkouts() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .role(UserRole.USER) // <--- Fixed: Added Role
                .build();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "user", "pass", UserRole.USER, true);

        when(userService.getById(userId)).thenReturn(user);

        mockMvc.perform(post("/workouts/new")
                        .param("name", "New Workout")
                        .param("description", "Desc")
                        .param("difficulty", "BEGINNER")
                        .param("duration", "30")
                        .param("public", "false")
                        .with(user(principal))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/workouts"));

        verify(workoutService).createWorkout(eq(user), any(CreateWorkoutRequest.class));
    }

    @Test
    void createWorkout_InvalidData_ReturnsFormView() throws Exception {
        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "user", "pass", UserRole.USER, true);

        mockMvc.perform(post("/workouts/new")
                        .param("name", "") // Invalid: Empty name
                        .with(user(principal))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("new-workout"));
    }

    @Test
    void updateWorkout_ValidData_Redirects() throws Exception {
        UUID workoutId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "user", "pass", UserRole.USER, true);

        mockMvc.perform(put("/workouts/{id}", workoutId)
                        .param("name", "Updated Name")
                        .param("description", "Updated Desc")
                        .param("difficulty", "INTERMEDIATE")
                        .param("duration", "45")
                        .with(user(principal))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/workouts"));

        verify(workoutService).editWorkoutDetails(eq(workoutId), any(CreateWorkoutRequest.class));
    }

    @Test
    void deleteWorkout_Redirects() throws Exception {
        UUID workoutId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "user", "pass", UserRole.USER, true);

        mockMvc.perform(delete("/workouts/{id}", workoutId)
                        .with(user(principal))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/workouts"));

        verify(workoutService).deleteWorkout(workoutId);
    }

    @Test
    void completeWorkout_CallsFeignClientAndRedirects() throws Exception {
        UUID workoutId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "user", "pass", UserRole.USER, true);

        Workout workout = Workout.builder().id(workoutId).name("Yoga").duration(30).build();
        when(workoutService.getById(workoutId)).thenReturn(workout);

        mockMvc.perform(post("/workouts/{id}/complete", workoutId)
                        .with(user(principal))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/workouts"));

        verify(historyClient).logWorkout(any(WorkoutLogRequest.class));
    }
}