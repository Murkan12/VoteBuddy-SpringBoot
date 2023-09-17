package com.ssi.votebuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private RoleName roleName;

    public Role() {}

    public Role(User userId, RoleName roleName) {
        this.userId = userId;
        this.roleName = roleName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public RoleName getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
}