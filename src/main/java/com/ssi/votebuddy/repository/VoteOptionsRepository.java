package com.ssi.votebuddy.repository;

import com.ssi.votebuddy.model.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteOptionsRepository extends JpaRepository<VoteOption, Long> {
}
