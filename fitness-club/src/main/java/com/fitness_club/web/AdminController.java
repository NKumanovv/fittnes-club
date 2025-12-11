package com.fitness_club.web;

import com.fitness_club.security.AuthenticationMetadata;
import com.fitness_club.user.model.User;
import com.fitness_club.user.model.UserRole;
import com.fitness_club.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ModelAndView getUsersPage(@AuthenticationPrincipal AuthenticationMetadata metadata) {

        User user = userService.getById(metadata.getUserId());

        ModelAndView modelAndView = new ModelAndView("admin-users");
        modelAndView.addObject("user", user);
        modelAndView.addObject("users", userService.getAllUsers());

        return modelAndView;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/{id}/toggle-status")
    public ModelAndView toggleUserStatus(@PathVariable UUID id) {
        userService.toggleUserStatus(id);
        return new ModelAndView("redirect:/admin/users");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/{id}/change-role")
    public ModelAndView changeUserRole(@PathVariable UUID id, @RequestParam UserRole role) {

        userService.changeUserRole(id, role);

        return new ModelAndView("redirect:/admin/users");
    }
}