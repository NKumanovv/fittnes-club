package com.fitness_club.scheduler;

import com.fitness_club.user.model.User;
import com.fitness_club.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserSchedulerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserScheduler userScheduler;

    @Test
    void sendWeeklyMotivation_IteratesUsersAndLogs() {
        User activeUser = User.builder().username("active").isActive(true).build();
        User inactiveUser = User.builder().username("inactive").isActive(false).build();
        List<User> users = Arrays.asList(activeUser, inactiveUser);

        when(userService.getAllUsers()).thenReturn(users);

        userScheduler.sendWeeklyMotivation();

        verify(userService).getAllUsers();
    }
}