package com.fitness_club.web;

import com.fitness_club.meal.model.Meal;
import com.fitness_club.meal.service.MealService;
import com.fitness_club.security.AuthenticationMetadata;
import com.fitness_club.user.model.User;
import com.fitness_club.user.model.UserRole;
import com.fitness_club.user.service.UserService;
import com.fitness_club.web.dto.CreateMealRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MealController.class)
public class MealControllerTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private MealService mealService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getMealsPage_ReturnsViewAndModel() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .username("testuser")
                .role(UserRole.USER)
                .build();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "testuser", "pass", UserRole.USER, true);

        when(userService.getById(userId)).thenReturn(user);
        when(mealService.getAllMealsByUserId(userId)).thenReturn(List.of(new Meal()));

        mockMvc.perform(get("/meals")
                        .with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(model().attributeExists("user", "meals"));
    }

    @Test
    void getMealPage_ReturnsMealView() throws Exception {
        UUID mealId = UUID.randomUUID();
        Meal meal = Meal.builder()
                .id(mealId)
                .name("Chicken Salad")
                .calories(500.0)
                .protein(30.0)
                .carbs(10.0)
                .fats(20.0)
                .build();

        when(mealService.getById(mealId)).thenReturn(meal);

        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "user", "pass", UserRole.USER, true);

        mockMvc.perform(get("/meals/{id}", mealId)
                        .with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("meal"))
                .andExpect(model().attributeExists("meal", "createMealRequest"));
    }

    @Test
    void getNewMealPage_ReturnsNewMealView() throws Exception {
        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "user", "pass", UserRole.USER, true);

        mockMvc.perform(get("/meals/new")
                        .with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("new-meal"))
                .andExpect(model().attributeExists("createMealRequest"));
    }

    @Test
    void createMeal_ValidData_RedirectsToMeals() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .role(UserRole.USER)
                .build();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "user", "pass", UserRole.USER, true);

        when(userService.getById(userId)).thenReturn(user);

        mockMvc.perform(post("/meals/new")
                        .param("name", "Oatmeal")
                        .param("calories", "300")
                        .param("protein", "10")
                        .param("carbs", "50")
                        .param("fats", "5")
                        .param("public", "false")
                        .with(user(principal))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meals"));

        verify(mealService).createMeal(eq(user), any(CreateMealRequest.class));
    }



    @Test
    void updateMeal_ValidData_Redirects() throws Exception {
        UUID mealId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "user", "pass", UserRole.USER, true);

        mockMvc.perform(put("/meals/{id}", mealId)
                        .param("name", "Updated Salad")
                        .param("calories", "400")
                        .param("protein", "25")
                        .param("carbs", "15")
                        .param("fats", "10")
                        .with(user(principal))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meals"));

        verify(mealService).editMealDetails(eq(mealId), any(CreateMealRequest.class));
    }

    @Test
    void deleteMeal_Redirects() throws Exception {
        UUID mealId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "user", "pass", UserRole.USER, true);

        mockMvc.perform(delete("/meals/{id}", mealId)
                        .with(user(principal))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meals"));

        verify(mealService).deleteMeal(mealId);
    }
}