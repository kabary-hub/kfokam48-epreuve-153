package com.presencekf.backend.controller;

import com.presencekf.backend.dto.RelecteurDto;
import com.presencekf.backend.service.RelecteurService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions/{sessionId}/relecteurs")
public class RelecteurController {

    private final RelecteurService relecteurService;

    public RelecteurController(RelecteurService relecteurService) {
        this.relecteurService = relecteurService;
    }

    @PostMapping
    public RelecteurDto affecterRelecteur(@PathVariable Long sessionId,
                                          @RequestParam String etudiant,
                                          @RequestParam Long exerciceId) {
        return relecteurService.affecterRelecteur(sessionId, etudiant, exerciceId);
    }

    @GetMapping("/{id}")
    public RelecteurDto getRelecteur(@PathVariable Long sessionId,
                                     @PathVariable Long id) {
        return relecteurService.getRelecteur(sessionId, id);
    }
}
