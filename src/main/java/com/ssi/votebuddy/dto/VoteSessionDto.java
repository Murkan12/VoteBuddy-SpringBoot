package com.ssi.votebuddy.dto;

import com.ssi.votebuddy.model.User;
import com.ssi.votebuddy.model.VoteOption;

import java.util.Set;
import java.util.UUID;

public record VoteSessionDto(UUID sessionId, User sessionOwner, Set<User> sessionVoters, Set<VoteOption> voteOptions) {
}
