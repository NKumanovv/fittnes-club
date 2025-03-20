package com.fitness_club.user.service;

import com.fitness_club.exeption.DomainException;
import com.fitness_club.user.model.User;
import com.fitness_club.user.repository.UserRepository;
import com.fitness_club.web.dto.RegisterRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }


    @Transactional
    public User register(RegisterRequest registerRequest){

        Optional<User> optionalUser = userRepository.findByUsername(registerRequest.getUsername());
        if (optionalUser.isPresent()){
            throw new DomainException(String.format("Username %s already exists.",registerRequest.getUsername()));
        }

        User user = userRepository.save(initializeUser(registerRequest));

        //subscriptionService.createDefaultSubscription(user);
        //walletService.createNewWallet(user);

        log.info(String.format("Successfully created new account for user [%s] with id [%s].", user.getUsername(), user.getId()));

        return user;
    }

    private User initializeUser(RegisterRequest registerRequest) {


        return User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())

                .build();
    }
}
