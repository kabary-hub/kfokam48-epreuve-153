package com.presencekf.backend.controller;

import com.presencekf.backend.dto.ManualPresenceDto;
import com.presencekf.backend.service.ManualPresenceService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions/{sessionId}/manual-presence")
public class ManualPresenceController {

    private final ManualPresenceService manualPresenceService;

    public ManualPresenceController(ManualPresenceService manualPresenceService) {
        this.manualPresenceService = manualPresenceService;
    }

    @PostMapping
    public ManualPresenceDto ajouterManuel(@PathVariable Long sessionId,
                                           @RequestParam String etudiant,
                                           @RequestParam String code) {
        return manualPresenceService.ajouterManuel(sessionId, etudiant, code);
    }
}
