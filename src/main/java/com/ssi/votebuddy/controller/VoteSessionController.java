package com.ssi.votebuddy.controller;

import com.ssi.votebuddy.error.AlreadyVotedException;
import com.ssi.votebuddy.error.LackOfDataException;
import com.ssi.votebuddy.error.ResourceNotFoundException;
import com.ssi.votebuddy.model.User;
import com.ssi.votebuddy.model.VoteOption;
import com.ssi.votebuddy.model.VoteSession;
import com.ssi.votebuddy.service.UserService;
import com.ssi.votebuddy.service.VoteSessionService;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/session")
public class VoteSessionController {

    private final VoteSessionService voteSessionService;

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(VoteSessionController.class);

    public VoteSessionController(VoteSessionService voteSessionService, UserService userService) {
        this.voteSessionService = voteSessionService;
        this.userService = userService;
    }

    @GetMapping("/listAll")
    public List<VoteSession> listSessions() {
        return voteSessionService.findAll();
    }

    @GetMapping("/ownedSessions")
    public ResponseEntity<?> getOwnedSessions(@RequestParam Long ownerId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(voteSessionService.getOwnedSessions(ownerId));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<?> createSession(@RequestParam("userId") Long userId, @RequestBody List<String> optionNames, @AuthenticationPrincipal User user) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            VoteSession voteSession = voteSessionService.create(userId, optionNames);
            logger.info(authentication.getPrincipal().toString());

            return ResponseEntity.status(HttpStatus.CREATED).body(voteSession);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (LackOfDataException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PatchMapping("/vote")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<?> vote(@RequestParam Long userId, @RequestParam UUID sessionId, @RequestParam Long voteOptionId) {
        try {
            VoteOption voteOption = voteSessionService.vote(userId, sessionId, voteOptionId);

            return ResponseEntity.status(HttpStatus.OK).body(voteOption);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (AlreadyVotedException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PatchMapping("/closeVote")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<?> closeVote(@RequestParam UUID sessionId, @RequestParam Long userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(voteSessionService.closeVoteSession(sessionId, userId));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
