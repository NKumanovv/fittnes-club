package com.fitness_club.web;

import com.fitness_club.security.AuthenticationMetadata;
import com.fitness_club.user.model.User;
import com.fitness_club.user.model.UserRole;
import com.fitness_club.user.service.UserService;
import com.fitness_club.web.dto.UserEditRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getProfileMenu_ReturnsViewAndModel() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .username("testuser")
                .role(UserRole.USER)
                .build();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "testuser", "pass", UserRole.USER, true);

        when(userService.getById(userId)).thenReturn(user);

        mockMvc.perform(get("/users/{id}/profile", userId)
                        .with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("profile-menu"))
                .andExpect(model().attributeExists("user", "userEditRequest"));
    }

    @Test
    void updateUserProfile_ValidData_RedirectsToHome() throws Exception {
        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "testuser", "pass", UserRole.USER, true);

        mockMvc.perform(put("/users/{id}/profile", userId)
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john@test.com")
                        .param("age", "25")
                        .param("height", "180")
                        .param("weight", "80")
                        .with(user(principal))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(userService).editUserDetails(eq(userId), any(UserEditRequest.class));
    }

    @Test
    void updateUserProfile_InvalidData_ReturnsProfileView() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).role(UserRole.USER).build();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "testuser", "pass", UserRole.USER, true);

        when(userService.getById(userId)).thenReturn(user);

        mockMvc.perform(put("/users/{id}/profile", userId)
                        .param("email", "not-an-email")
                        .with(user(principal))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("profile-menu"))
                .andExpect(model().attributeExists("user", "userEditRequest"));
    }
}