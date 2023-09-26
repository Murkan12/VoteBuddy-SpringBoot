package com.ssi.votebuddy.controller;

import com.ssi.votebuddy.model.User;
import com.ssi.votebuddy.repository.UserRepository;
import com.ssi.votebuddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        logger.info(user.getEmail());

        return userService.create(user);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
         return ResponseEntity.status(HttpStatus.OK).body(authentication.getPrincipal());
    }

    @GetMapping("/listUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> listUsers() {
        return userRepository.findAll();
    }
}
