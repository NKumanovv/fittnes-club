package com.fitness_club.user.service;

import com.fitness_club.exeption.DomainException;
import com.fitness_club.meal.model.Meal;
import com.fitness_club.meal.service.MealService;
import com.fitness_club.security.AuthenticationMetadata;
import com.fitness_club.user.model.User;
import com.fitness_club.user.model.UserRole;
import com.fitness_club.user.repository.UserRepository;
import com.fitness_club.web.dto.RegisterRequest;
import com.fitness_club.web.dto.UserEditRequest;
import com.fitness_club.workout.repository.WorkoutRepository;
import com.fitness_club.workout.service.WorkoutService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MealService mealService;
    private final WorkoutService workoutService;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MealService mealService, WorkoutService workoutService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mealService = mealService;
        this.workoutService = workoutService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new DomainException("User with this username does not exist."));

        return new AuthenticationMetadata(user.getId(), username, user.getPassword(), user.getRole());
    }


    @Transactional
    public User register(RegisterRequest registerRequest){

        Optional<User> optionalUser = userRepository.findByUsername(registerRequest.getUsername());

        if (optionalUser.isPresent()){
            throw new DomainException(String.format("Username %s already exists.",registerRequest.getUsername()));
        }

        User user = userRepository.save(initializeUser(registerRequest));
        mealService.createFirstMeal(user);
        workoutService.createFirstWorkout(user);

        log.info(String.format("Successfully created new account for user [%s] with id [%s].", user.getUsername(), user.getId()));

        return user;
    }

    private User initializeUser(RegisterRequest registerRequest) {

        return User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .age(registerRequest.getAge())
                .height(registerRequest.getHeight())
                .weight(registerRequest.getWeight())
                .gender(registerRequest.getGender())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .createdOn(LocalDateTime.now())
                .build();
    }

    public User getById(UUID id) {

        return userRepository.findById(id).orElseThrow(() -> new DomainException("User with id [%s] does not exist.".formatted(id)));
    }

    //@CacheEvict(value = "users", allEntries = true)
    public void editUserDetails(UUID userId, UserEditRequest userEditRequest) {

        User user = getById(userId);

        //if (userEditRequest.getEmail().isBlank()) {
        //    notificationService.saveNotificationPreference(userId, false, null);
        //}

        user.setFirstName(userEditRequest.getFirstName());
        user.setLastName(userEditRequest.getLastName());
        user.setAge(userEditRequest.getAge());
        user.setHeight(userEditRequest.getHeight());
        user.setWeight(userEditRequest.getWeight());
        user.setEmail(userEditRequest.getEmail());
        user.setProfilePicture(userEditRequest.getProfilePicture());

        //if (!userEditRequest.getEmail().isBlank()) {
        //    notificationService.saveNotificationPreference(userId, true, userEditRequest.getEmail());
        //}

        userRepository.save(user);
    }
}
