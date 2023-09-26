package com.ssi.votebuddy.repository;

import com.ssi.votebuddy.model.User;
import com.ssi.votebuddy.model.VoteSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VoteSessionRepository extends JpaRepository<VoteSession, UUID> { }
