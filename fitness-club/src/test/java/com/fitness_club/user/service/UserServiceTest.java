package com.fitness_club.user.service;

import com.fitness_club.exeption.DomainException;
import com.fitness_club.meal.service.MealService;
import com.fitness_club.security.AuthenticationMetadata;
import com.fitness_club.user.model.User;
import com.fitness_club.user.model.UserRole;
import com.fitness_club.user.repository.UserRepository;
import com.fitness_club.web.dto.RegisterRequest;
import com.fitness_club.web.dto.UserEditRequest;
import com.fitness_club.workout.service.WorkoutService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MealService mealService;

    @Mock
    private WorkoutService workoutService;

    @InjectMocks
    private UserService userService;


    @Test
    void loadUserByUsername_UserExists_ReturnsMetadata() {
        String username = "testuser";
        User user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .password("encodedPass")
                .role(UserRole.USER)
                .isActive(true)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername(username);

        assertNotNull(result);
        assertInstanceOf(AuthenticationMetadata.class, result);
        assertEquals(username, result.getUsername());
        assertEquals("encodedPass", result.getPassword());
        assertTrue(result.isEnabled()); // Checks isActive
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsException() {
        String username = "unknown";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername(username));
    }


    @Test
    void register_NewUser_SavesUserAndCreatesDefaults() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("newuser");
        req.setPassword("password");
        req.setEmail("test@test.com");

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedHash");

        User savedUser = User.builder().id(UUID.randomUUID()).username("newuser").build();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.register(req);

        assertNotNull(result);
        verify(userRepository).save(any(User.class));
        verify(mealService).createFirstMeal(savedUser); // Verify integration
        verify(workoutService).createFirstWorkout(savedUser); // Verify integration
    }

    @Test
    void register_UsernameTaken_ThrowsDomainException() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("existing");

        when(userRepository.findByUsername("existing")).thenReturn(Optional.of(new User()));

        assertThrows(DomainException.class, () -> userService.register(req));
        verify(userRepository, never()).save(any());
    }


    @Test
    void getById_UserExists_ReturnsUser() {
        UUID id = UUID.randomUUID();
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = userService.getById(id);

        assertEquals(user, result);
    }

    @Test
    void getById_NotFound_ThrowsDomainException() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> userService.getById(id));
    }


    @Test
    void editUserDetails_ValidRequest_UpdatesFields() {
        UUID id = UUID.randomUUID();
        UserEditRequest req = new UserEditRequest();
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setEmail("john@test.com");

        User existingUser = new User();
        existingUser.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));

        userService.editUserDetails(id, req);

        assertEquals("John", existingUser.getFirstName());
        assertEquals("john@test.com", existingUser.getEmail());
        verify(userRepository).save(existingUser);
    }


    @Test
    void toggleUserStatus_ActiveUser_BecomesInactive() {
        UUID id = UUID.randomUUID();
        User user = User.builder().id(id).isActive(true).build();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.toggleUserStatus(id);

        assertFalse(user.isActive());
        verify(userRepository).save(user);
    }

    @Test
    void changeUserRole_ToAdmin_UpdatesRole() {
        UUID id = UUID.randomUUID();
        User user = User.builder().id(id).role(UserRole.USER).build();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.changeUserRole(id, UserRole.ADMIN);

        assertEquals(UserRole.ADMIN, user.getRole());
        verify(userRepository).save(user);
    }

    @Test
    void getAllUsers_ReturnsList() {
        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
    }
}