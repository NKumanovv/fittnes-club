package com.fitness_club.workout.service;

import com.fitness_club.exeption.DomainException;
import com.fitness_club.user.model.User;
import com.fitness_club.web.dto.CreateWorkoutRequest;
import com.fitness_club.workout.model.Difficulty;
import com.fitness_club.workout.model.Workout;
import com.fitness_club.workout.repository.WorkoutRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @InjectMocks
    private WorkoutService workoutService;

    @Test
    void createWorkout_ValidRequest_SavesWorkout() {
        User user = new User();
        user.setUsername("testuser");

        CreateWorkoutRequest request = new CreateWorkoutRequest();
        request.setName("Chest Day");
        request.setDescription("Heavy lifting");
        request.setDifficulty(Difficulty.INTERMEDIATE);
        request.setDuration(60);
        request.setPublic(true);

        when(workoutRepository.save(any(Workout.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Workout result = workoutService.createWorkout(user, request);

        assertNotNull(result);
        assertEquals("Chest Day", result.getName());
        assertEquals(Difficulty.INTERMEDIATE, result.getDifficulty());
        assertTrue(result.isPublic());
        assertEquals(user, result.getUser());
        verify(workoutRepository).save(any(Workout.class));
    }

    @Test
    void getById_WorkoutExists_ReturnsWorkout() {
        UUID id = UUID.randomUUID();
        Workout workout = new Workout();
        workout.setId(id);

        when(workoutRepository.findById(id)).thenReturn(Optional.of(workout));

        Workout result = workoutService.getById(id);

        assertEquals(workout, result);
    }

    @Test
    void getById_NotFound_ThrowsDomainException() {
        UUID id = UUID.randomUUID();
        when(workoutRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> workoutService.getById(id));
    }

    @Test
    void editWorkoutDetails_ExistingWorkout_UpdatesFieldsAndSaves() {
        UUID id = UUID.randomUUID();
        Workout existingWorkout = new Workout();
        existingWorkout.setId(id);
        existingWorkout.setName("Old Name");

        CreateWorkoutRequest updateRequest = new CreateWorkoutRequest();
        updateRequest.setName("New Name");
        updateRequest.setDescription("New Desc");
        updateRequest.setDifficulty(Difficulty.ADVANCED);
        updateRequest.setDuration(90);
        updateRequest.setPublic(false);

        when(workoutRepository.findById(id)).thenReturn(Optional.of(existingWorkout));

        workoutService.editWorkoutDetails(id, updateRequest);

        assertEquals("New Name", existingWorkout.getName());
        assertEquals(90, existingWorkout.getDuration());
        assertFalse(existingWorkout.isPublic());
        verify(workoutRepository).save(existingWorkout);
    }

    @Test
    void deleteWorkout_ValidId_CallsRepository() {
        UUID id = UUID.randomUUID();

        workoutService.deleteWorkout(id);

        verify(workoutRepository).deleteById(id);
    }

    @Test
    void createFirstWorkout_SavesDefaultWorkout() {
        User user = new User();

        workoutService.createFirstWorkout(user);

        verify(workoutRepository).save(argThat(workout ->
                workout.getName().equals("Bicep workout") &&
                        workout.getDuration() == 60 &&
                        workout.getUser() == user
        ));
    }

    @Test
    void getAllWorkoutsById_ReturnsList() {
        UUID userId = UUID.randomUUID();
        when(workoutRepository.getAllWorkoutsByUser_Id(userId)).thenReturn(List.of(new Workout(), new Workout()));

        List<Workout> result = workoutService.getAllWorkoutsById(userId);

        assertEquals(2, result.size());
    }

    @Test
    void getAllPublicWorkouts_ReturnsList() {
        when(workoutRepository.findByIsPublicTrue()).thenReturn(List.of(new Workout()));

        List<Workout> result = workoutService.getAllPublicWorkouts();

        assertEquals(1, result.size());
    }
}