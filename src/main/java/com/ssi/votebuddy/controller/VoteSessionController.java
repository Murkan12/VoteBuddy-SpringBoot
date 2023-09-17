package com.ssi.votebuddy.controller;

import com.ssi.votebuddy.error.AlreadyVotedException;
import com.ssi.votebuddy.error.LackOfDataException;
import com.ssi.votebuddy.error.ResourceNotFoundException;
import com.ssi.votebuddy.model.User;
import com.ssi.votebuddy.model.VoteOption;
import com.ssi.votebuddy.model.VoteSession;
import com.ssi.votebuddy.service.VoteSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/session")
public class VoteSessionController {

    private final VoteSessionService voteSessionService;

    private static final Logger logger = LoggerFactory.getLogger(VoteSessionController.class);

    public VoteSessionController(VoteSessionService voteSessionService) {
        this.voteSessionService = voteSessionService;
    }

    @GetMapping
    public List<VoteSession> listSessions() {
        return voteSessionService.findAll();
    }

    @PostMapping
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<?> createSession(@RequestParam("userId") Long userId, @RequestBody List<String> optionNames, @AuthenticationPrincipal User user) {
        try {
            VoteSession voteSession = voteSessionService.create(userId, optionNames);
            logger.info(voteSession.getSessionOwner().getEmail());
            logger.info(user.getId().toString());

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
    public ResponseEntity<?> vote(@RequestParam Long userId, UUID sessionId, Long voteOptionId) {
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
}
