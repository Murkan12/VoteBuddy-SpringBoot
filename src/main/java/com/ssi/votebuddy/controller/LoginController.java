package com.ssi.votebuddy.controller;

import com.ssi.votebuddy.model.User;
import com.ssi.votebuddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        logger.info(user.getEmail());

        return userService.create(user);
    }

    @GetMapping("/")
    public List<User> listUsers() {
        return userService.findAll();
    }
}
