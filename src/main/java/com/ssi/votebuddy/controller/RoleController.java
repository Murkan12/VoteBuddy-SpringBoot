package com.ssi.votebuddy.controller;

import com.ssi.votebuddy.error.ResourceNotFoundException;
import com.ssi.votebuddy.model.Role;
import com.ssi.votebuddy.repository.RoleRepository;
import com.ssi.votebuddy.service.UserService;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.RollbackException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    public final RoleRepository roleRepository;
    private final UserService userService;

    public RoleController(RoleRepository roleRepository, UserService userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @GetMapping
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @PostMapping("/addAdminRole")
    public ResponseEntity<?> addAdminRoleToUser(@RequestParam Long userId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.addAdminRole(userId));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
