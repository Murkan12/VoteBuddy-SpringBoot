package com.ssi.votebuddy.service;

import com.ssi.votebuddy.model.User;
import com.ssi.votebuddy.model.VoteSession;
import com.ssi.votebuddy.repository.UserRepository;
import com.ssi.votebuddy.repository.VoteSessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteSessionService {

    private final VoteSessionRepository sessionRepository;
    private final UserRepository userRepository;

    public VoteSessionService(VoteSessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    public VoteSession create(Long userId) throws Exception{
        User user = userRepository.findById(userId).orElse(null);

        VoteSession newSession = new VoteSession();

        if (user != null) {
            newSession.setSessionOwner(user);

            return sessionRepository.save(newSession);
        } else {
            throw new Exception("User with given id not found");
        }
    }

    public void addSessionVoter(Long userId, Long sessionId) throws Exception {
        VoteSession voteSession = sessionRepository.findById(sessionId).orElse(null);
        User votingUser = userRepository.findById(userId).orElse(null);

        if (voteSession != null && votingUser != null) {
            voteSession.addSessionVoters(votingUser);
            sessionRepository.save(voteSession);
        } else if (voteSession == null) {
             throw new Exception("Session not found");
        } else {
            throw new Exception("User not found");
        }
    }

    public VoteSession find(long sessionId) {
        return sessionRepository.findById(sessionId).orElse(null);
    }

    public List<VoteSession> findAll() {
        return sessionRepository.findAll();
    }
}
