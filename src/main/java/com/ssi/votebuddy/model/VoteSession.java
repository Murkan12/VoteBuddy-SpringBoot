package com.ssi.votebuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "vote_sessions")
public class VoteSession {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "session_id", columnDefinition = "BINARY(16)")
    private UUID sessionId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User sessionOwner;

    @JsonIgnore
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "user_vote_sessions",
    joinColumns = @JoinColumn(name = "session_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "session_voters")
    private Set<User> sessionVoters;

    @OneToMany(mappedBy = "voteSession", cascade = CascadeType.ALL)
    private Set<VoteOption> voteOptions;

    @Column(name = "is_open")
    private Boolean isOpen = true;

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public User getSessionOwner() {
        return sessionOwner;
    }

    public void setSessionOwner(User sessionOwner) {
        this.sessionOwner = sessionOwner;
    }

    public Set<User> getSessionVoters() {
        return sessionVoters;
    }

    public void addSessionVoters(User user) {
        sessionVoters.add(user);
    }

    public Set<VoteOption> getVoteOptions() {
        return voteOptions;
    }

    public void setVoteOptions(Set<VoteOption> voteOptions) {
        this.voteOptions = voteOptions;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }
}
