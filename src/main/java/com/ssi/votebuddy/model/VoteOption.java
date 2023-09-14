package com.ssi.votebuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "vote_options")
public class VoteOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "session_id")
    private VoteSession voteSession;

    @Column(name = "option_name")
    private String optionName;

    @Column(name = "number_of_votes", columnDefinition = "INT DEFAULT 0")
    private Integer numberOfVotes;

    public VoteOption() {}

    public VoteOption(String optionName, VoteSession voteSession) {
        this.setOptionName(optionName);
        this.setVoteSession(voteSession);
        this.setNumberOfVotes(0);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public VoteSession getVoteSession() {
        return voteSession;
    }

    public void setVoteSession(VoteSession sessionId) {
        this.voteSession = sessionId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String option) {
        this.optionName = option;
    }

    public Integer getNumberOfVotes() {
        return numberOfVotes;
    }

    public void setNumberOfVotes(Integer numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }

    public void incrementNumberOfVotes() {
        if (this.numberOfVotes == null) {
            this.setNumberOfVotes(0);
        }

        this.numberOfVotes += 1;
    }

}
