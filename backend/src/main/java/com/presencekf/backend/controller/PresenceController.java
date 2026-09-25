package com.presencekf.backend.controller;

import com.presencekf.backend.dto.PresenceDto;
import com.presencekf.backend.service.PresenceService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions/{sessionId}/presences")
public class PresenceController {

    private final PresenceService presenceService;

    public PresenceController(PresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @PostMapping
    public PresenceDto marquerPresence(@PathVariable Long sessionId,
                                       @RequestParam String etudiant,
                                       @RequestParam String code) {
        return presenceService.marquerPresence(sessionId, etudiant, code);
    }

    @GetMapping("/{id}")
    public PresenceDto getPresence(@PathVariable Long sessionId,
                                   @PathVariable Long id) {
        return presenceService.getPresence(sessionId, id);
    }
}
