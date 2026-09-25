package com.presencekf.backend.controller;

import com.presencekf.backend.dto.SessionDto;
import com.presencekf.backend.service.SessionService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public SessionDto ouvrirSession(@RequestParam String code,
                                    @RequestParam LocalDateTime debut,
                                    @RequestParam LocalDateTime fin) {
        return sessionService.ouvrirSession(code, debut, fin);
    }

    @PutMapping("/{id}/cloture")
    public SessionDto clôturerSession(@PathVariable Long id) {
        return sessionService.clôturerSession(id);
    }

    @PostMapping("/{id}/presence")
    public SessionDto marquerPresence(@PathVariable Long id,
                                      @RequestParam String etudiant,
                                      @RequestParam String code) {
        return sessionService.marquerPresence(code, etudiant);
    }

    @GetMapping("/{code}")
    public SessionDto getSessionByCode(@PathVariable String code) {
        return sessionService.getSessionByCode(code);
    }

    @GetMapping
    public List<SessionDto> getAllSessions() {
        return sessionService.getAllSessions();
    }
}
