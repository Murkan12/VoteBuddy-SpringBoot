package com.ssi.votebuddy.service;

import com.ssi.votebuddy.error.AlreadyVotedException;
import com.ssi.votebuddy.error.LackOfDataException;
import com.ssi.votebuddy.error.ResourceNotFoundException;
import com.ssi.votebuddy.model.User;
import com.ssi.votebuddy.model.VoteOption;
import com.ssi.votebuddy.model.VoteSession;
import com.ssi.votebuddy.repository.UserRepository;
import com.ssi.votebuddy.repository.VoteOptionsRepository;
import com.ssi.votebuddy.repository.VoteSessionRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VoteSessionService {

    private final VoteSessionRepository sessionRepository;
    private final UserRepository userRepository;

    private final VoteOptionsRepository optionsRepository;

    public VoteSessionService(VoteSessionRepository sessionRepository, UserRepository userRepository, VoteOptionsRepository optionsRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.optionsRepository = optionsRepository;
    }

    public VoteSession create(Long userId, List<String> optionNames) throws Exception {
        User user = userRepository.findById(userId).orElse(null);

        VoteSession newSession = new VoteSession();

        if (user != null) {
            newSession.setSessionOwner(user);
            VoteSession savedSession = sessionRepository.save(newSession);

            Set<VoteOption> voteOptions = new HashSet<>();

            if (!optionNames.isEmpty()) {
                for (String option : optionNames) {
                    VoteOption voteOption = new VoteOption(option, savedSession);
                    voteOptions.add(voteOption);
                }

                optionsRepository.saveAll(voteOptions);
                return savedSession;
            } else {
                throw new LackOfDataException("Set of vote options cannot be empty");
            }
        } else {
            throw new ResourceNotFoundException("User with given id not found");
        }
    }

    public VoteOption vote(Long userId, UUID sessionId, Long voteOptionId) throws Exception {
        VoteSession voteSession = sessionRepository.findById(sessionId).orElse(null);
        User votingUser = userRepository.findById(userId).orElse(null);

        if (voteSession != null && votingUser != null) {
            boolean userInSession = checkIfUserInSession(votingUser, voteSession);
            boolean isOpen = checkIfIsOpen(sessionId);
            VoteOption voteOption = getVoteOption(voteSession, voteOptionId);

            if (!userInSession && voteOption != null && isOpen) {
                voteSession.addSessionVoters(votingUser);
                voteOption.incrementNumberOfVotes();
                sessionRepository.save(voteSession);

                return optionsRepository.save(voteOption);
            } else if (userInSession) {
                throw new AlreadyVotedException("User already voted");
            } else if (!isOpen) {
                throw new ResourceNotFoundException("Vote session is closed");
            } else {
                throw new ResourceNotFoundException("Option with given id doesn't exist");
            }
        } else if (voteSession == null) {
             throw new ResourceNotFoundException("Session not found");
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }

    @PreAuthorize("#ownerId == authentication.principal.id")
    public VoteSession closeVoteSession(UUID sessionId, Long ownerId) throws ResourceNotFoundException, BadCredentialsException {
        VoteSession voteSession = sessionRepository.findById(sessionId).orElse(null);
        User user = userRepository.findById(ownerId).orElse(null);

        if (voteSession != null && user != null) {
            if (voteSession.getSessionOwner().equals(user)) {
                voteSession.setIsOpen(false);

                return sessionRepository.save(voteSession);
            } else {
                throw new BadCredentialsException("User is not session owner");
            }
        } else if (user == null) {
            throw new ResourceNotFoundException("User with given id not found");
        } else {
            throw new ResourceNotFoundException("Vote session with given id not found");
        }
    }

    public VoteSession find(UUID sessionId) {
        return sessionRepository.findById(sessionId).orElse(null);
    }

    public Set<VoteSession> getOwnedSessions(Long userId) throws ResourceNotFoundException {
        User sessionOwner = userRepository.findById(userId).orElse(null);

        if (sessionOwner != null) {
            Set<VoteSession> openSessions = new HashSet<>();
            for (VoteSession session : sessionOwner.getOwnedSessions()) {
                if (checkIfIsOpen(session.getSessionId())) {
                    openSessions.add(session);
                }
            }

            return openSessions;
        } else {
            throw new ResourceNotFoundException("User with given id not found");
        }
    }

    public List<VoteSession> findAll() {
        return sessionRepository.findAll();
    }

    private void incrementVote(VoteSession voteSession, Long voteOptionId) {
        Set<VoteOption> voteOptions = voteSession.getVoteOptions();

        for (VoteOption voteOption : voteOptions) {
            if (Objects.equals(voteOption.getId(), voteOptionId)) {
                voteOption.incrementNumberOfVotes();
                return;
            }
        }
    }

    private VoteOption getVoteOption(VoteSession voteSession, Long optionId) {
        Set<VoteOption> voteOptions = voteSession.getVoteOptions();

        for (VoteOption voteOption : voteOptions) {
            if (Objects.equals(voteOption.getId(), optionId))
                return voteOption;
        }

        return null;
    }

    private boolean checkIfUserInSession(User testedUser, VoteSession voteSession) {
        Set<User> users = voteSession.getSessionVoters();
        return users.contains(testedUser);
    }

    private boolean checkIfIsOpen(UUID sessionId) {
        VoteSession voteSession = sessionRepository.findById(sessionId).orElse(null);

        if (voteSession != null) {
            return voteSession.getIsOpen();
        }

        return false;
    }


}
