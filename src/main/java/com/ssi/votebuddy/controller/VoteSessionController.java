package com.ssi.votebuddy.controller;

import com.ssi.votebuddy.model.VoteSession;
import com.ssi.votebuddy.service.VoteSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> createSession(@RequestParam Long userId) {
        try {
            VoteSession voteSession = voteSessionService.create(userId);
            logger.info(voteSession.getSessionOwner().getEmail());

            return ResponseEntity.status(HttpStatus.CREATED).body(voteSession);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }


}
