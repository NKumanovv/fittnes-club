package com.fitness_club.web;

import com.fitness_club.security.AuthenticationMetadata;
import com.fitness_club.user.model.User;
import com.fitness_club.user.model.UserRole;
import com.fitness_club.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getUsersPage_AuthorizedRequest_ShouldReturnAdminView() throws Exception {
        UUID adminId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(adminId, "AdminUser", "pass", UserRole.ADMIN, true);

        User adminUser = User.builder().id(adminId).username("AdminUser").role(UserRole.ADMIN).build();
        when(userService.getById(adminId)).thenReturn(adminUser);
        when(userService.getAllUsers()).thenReturn(List.of(adminUser));

        MockHttpServletRequestBuilder request = get("/admin/users")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("admin-users"))
                .andExpect(model().attributeExists("user", "users"));
    }

    @Test
    void toggleUserStatus_AuthorizedRequest_ShouldRedirect() throws Exception {
        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "AdminUser", "pass", UserRole.ADMIN, true);
        UUID targetUserId = UUID.randomUUID();

        MockHttpServletRequestBuilder request = post("/admin/users/{id}/toggle-status", targetUserId)
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        verify(userService, times(1)).toggleUserStatus(targetUserId);
    }

    @Test
    void changeUserRole_AuthorizedRequest_ShouldRedirect() throws Exception {
        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "AdminUser", "pass", UserRole.ADMIN, true);
        UUID targetUserId = UUID.randomUUID();

        MockHttpServletRequestBuilder request = post("/admin/users/{id}/change-role", targetUserId)
                .param("role", "ADMIN")
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        verify(userService, times(1)).changeUserRole(targetUserId, UserRole.ADMIN);
    }

    @Test
    void getUsersPage_UnauthorizedRequest_ShouldReturnForbidden() throws Exception {
        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "NormalUser", "pass", UserRole.USER, true);

        MockHttpServletRequestBuilder request = get("/admin/users")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }
}