package com.fitness_club;

import com.fitness_club.meal.model.Meal;
import com.fitness_club.meal.service.MealService;
import com.fitness_club.user.model.User;
import com.fitness_club.user.service.UserService;
import com.fitness_club.web.dto.RegisterRequest;
import com.fitness_club.workout.model.Workout;
import com.fitness_club.workout.service.WorkoutService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RegisterITest {

    @Autowired
    private UserService userService;

    @Autowired
    private MealService mealService;

    @Autowired
    private WorkoutService workoutService;

    @Test
    void registerUser_HappyPath_CreatesUserAndDefaults() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("IntegrationUser");
        request.setPassword("password123");
        request.setEmail("integration@test.com");
        request.setAge(25);
        request.setHeight(180);
        request.setWeight(80);

        User registeredUser = userService.register(request);

        assertNotNull(registeredUser.getId());
        assertEquals("IntegrationUser", registeredUser.getUsername());

        List<Meal> userMeals = mealService.getAllMealsByUserId(registeredUser.getId());
        assertFalse(userMeals.isEmpty());
        assertEquals("Chicken and rice", userMeals.get(0).getName());

        List<Workout> userWorkouts = workoutService.getAllWorkoutsById(registeredUser.getId());
        assertFalse(userWorkouts.isEmpty());
        assertEquals("Bicep workout", userWorkouts.get(0).getName());
    }
}