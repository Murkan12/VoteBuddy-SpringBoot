package com.ssi.votebuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_pwd")
    private String pwd;

    @JsonIgnore
    @OneToMany(mappedBy = "sessionOwner", cascade = CascadeType.ALL)
    private Set<VoteSession> ownedSessions;

    @ManyToMany(mappedBy = "sessionVoters")
    private Set<VoteSession> voteSessions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Set<VoteSession> getOwnedSessions() {
        return ownedSessions;
    }

    public void addOwnedSession(VoteSession voteSession) {
        ownedSessions.add(voteSession);
    }

    public Set<VoteSession> getVoteSessions() {
        return voteSessions;
    }

    public void addVoteSession(VoteSession voteSession) {
        voteSessions.add(voteSession);
    }
}
