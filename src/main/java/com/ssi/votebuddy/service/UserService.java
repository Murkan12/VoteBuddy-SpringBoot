package com.ssi.votebuddy.service;

import com.ssi.votebuddy.repository.UserRepository;
import com.ssi.votebuddy.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPwd());
        user.setPwd(hashedPassword);

        return userRepository.save(user);
    }

    public User find(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
