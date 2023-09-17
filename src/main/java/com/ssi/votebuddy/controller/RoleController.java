package com.ssi.votebuddy.controller;

import com.ssi.votebuddy.model.Role;
import com.ssi.votebuddy.repository.RoleRepository;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.RollbackException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    public final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}
