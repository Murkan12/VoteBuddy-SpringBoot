package com.ssi.votebuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Map;

@Entity
public class VoteOptions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "session_id")
    private VoteSession sessionId;

    //todo finish setting relations && make git repo

    private String option;

    @Column(name = "number_of_votes")
    private Integer numberOfVotes;
}
