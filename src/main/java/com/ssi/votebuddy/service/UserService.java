package com.ssi.votebuddy.service;

import com.ssi.votebuddy.error.ResourceNotFoundException;
import com.ssi.votebuddy.model.Role;
import com.ssi.votebuddy.model.RoleName;
import com.ssi.votebuddy.model.VoteSession;
import com.ssi.votebuddy.repository.RoleRepository;
import com.ssi.votebuddy.repository.UserRepository;
import com.ssi.votebuddy.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPwd());
        user.setPwd(hashedPassword);

        User userToAddRole = userRepository.save(user);
        return addUserRole(userToAddRole);
    }

    public Long getAuthenticatedUserId(Authentication authentication) {
        String userEmail = authentication.getName();
        User authedUser = userRepository.findByEmail(userEmail);

        return authedUser.getId();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Role addAdminRole(Long userId) throws ResourceNotFoundException {
        User userToUpdate = userRepository.findById(userId).orElse(null);

        if (userToUpdate != null) {
            return roleRepository.save(new Role(userToUpdate, RoleName.ROLE_ADMIN));
        } else {
            throw new ResourceNotFoundException("User with given id not found");
        }
    }

    private User addUserRole(User user) {
        Set<Role> roleSet = new HashSet<>();
        Role userRole = new Role(user, RoleName.ROLE_USER);
        roleSet.add(userRole);
        roleRepository.save(userRole);
        user.setRole(roleSet);

        return user;
    }
}